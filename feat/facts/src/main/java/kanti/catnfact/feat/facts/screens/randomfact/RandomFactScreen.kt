package kanti.catnfact.feat.facts.screens.randomfact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.facts.FactsDestinations
import kanti.catnfact.feat.facts.R
import kanti.catnfact.ui.components.error.ErrorPanel
import kanti.catnfact.ui.components.error.ErrorState
import kanti.catnfact.ui.components.fact.FactUiState
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun RandomFactScreen(
	toSettings: () -> Unit = {},
	navController: NavController = rememberNavController()
) {
	val viewModel = hiltViewModel<RandomFactViewModel>()
	val state by viewModel.factUiState.collectAsState()

	RandomFactContent(
		state = state,
		onScreenAction = { intent ->
			when (intent) {
				is ToSettingsIntent -> toSettings()
				is ToFactListIntent -> {
					navController.navigate(route = FactsDestinations.FACT_LIST)
				}
			}
		},
		onFactAction = { intent ->
			viewModel.onAction(intent)
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFactContent(
	state: RandomFactUiState,
	onScreenAction: (FactScreenIntent) -> Unit,
	onFactAction: (RandomFactIntent) -> Unit
) {
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text(text = stringResource(id = R.string.fact)) },
				actions = {
					var expandDropdownMenu by remember { mutableStateOf(false) }

					IconButton(onClick = { expandDropdownMenu = !expandDropdownMenu }) {
						Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
					}

					DropdownMenu(
						expanded = expandDropdownMenu,
						onDismissRequest = { expandDropdownMenu = false }
					) {
						DropdownMenuItem(
							text = { Text(text = stringResource(id = R.string.settings)) },
							leadingIcon = {
								Icon(
									imageVector = Icons.Default.Settings,
									contentDescription = null
								)
							},
							onClick = {
								onScreenAction(ToSettingsIntent)
							}
						)
					}
				}
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		) {
			Column(
				modifier = Modifier
					.padding(
						vertical = 12.dp,
						horizontal = 16.dp
					)
			) {
				Box(
					modifier = Modifier.fillMaxWidth()
				) {
					Text(
						text = state.fact.fact,
						style = MaterialTheme.typography.headlineMedium
					)
					if (state.isLoading) {
						CircularProgressIndicator(
							modifier = Modifier.align(Alignment.Center)
						)
					}
				}

				Spacer(modifier = Modifier.height(12.dp))

				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					IconButton(
						onClick = { onFactAction(ChangeFavouriteIntent(state.fact.hash))  },
						colors = IconButtonDefaults.filledTonalIconButtonColors(),
						enabled = !state.isLoading
					) {
						val painter = if (state.fact.isFavourite)
							painterResource(id = R.drawable.round_star_24)
						else painterResource(id = R.drawable.round_star_outline_24)

						Icon(painter = painter, contentDescription = null)
					}

					Row(
						modifier = Modifier
							.weight(1f),
						horizontalArrangement = Arrangement.End
					) {
						FilledTonalButton(
							onClick = { onScreenAction(ToFactListIntent) }
						) {
							Text(text = stringResource(id = R.string.see_all_facts))
						}

						Spacer(modifier = Modifier.width(8.dp))

						Button(
							onClick = { onFactAction(NextRandomFactIntent) },
							enabled = !state.isLoading,
							contentPadding = PaddingValues(
								end = 16.dp,
								start = 24.dp
							)
						) {
							Text(text = stringResource(id = R.string.next_fact))
							Spacer(modifier = Modifier.width(8.dp))
							Icon(
								painter = painterResource(id = R.drawable.next),
								contentDescription = null
							)
						}
					}
				}
			}

			AnimatedVisibility(
				modifier = Modifier.align(Alignment.BottomCenter),
				visible = state.isNoConnection,
				enter = slideInVertically { it },
				exit = slideOutVertically { it }
			) {
				ErrorPanel(
					state = ErrorState(
						title = stringResource(id = R.string.no_connection),
						callbackLabel = stringResource(id = R.string.refresh)
					),
					onCallback =  {
						onFactAction(NextRandomFactIntent)
					}
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewRandomFactContent() {
	CatNFactTheme {
		RandomFactContent(
			state = RandomFactUiState(
				fact = FactUiState(
					hash = "131531",
					fact = "The ability of a cat to find its way home is called “psi-traveling.” " +
							"Experts think cats either use the angle of the sunlight to find their way " +
							"or that cats have magnetized cells in their brains that act as compasses.",
					isFavourite = false
				),
				isLoading = true,
				isNoConnection = true
			),
			onScreenAction = {},
			onFactAction = {}
		)
	}
}
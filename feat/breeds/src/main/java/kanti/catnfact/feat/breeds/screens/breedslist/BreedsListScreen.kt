package kanti.catnfact.feat.breeds.screens.breedslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import kanti.catnfact.feat.breeds.R
import kanti.catnfact.ui.components.breed.BreedCard
import kanti.catnfact.ui.components.breed.BreedCardDefault
import kanti.catnfact.ui.components.breed.BreedUiState
import kanti.catnfact.ui.components.error.ErrorPanel
import kanti.catnfact.ui.components.error.ErrorState
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun BreedsListScreen(
	toSettings: () -> Unit
) {
	val viewModel = hiltViewModel<BreedsListViewModel>()
	val state by viewModel.breedsUiState.collectAsState()

	LifecycleStartEffect(viewModel) {
		if (state.isNoConnection)
			viewModel.onAction(OnRefreshIntent)
		onStopOrDispose {  }
	}

	BreedsListContent(
		state = state,
		onScreenAction = { intent ->
			when (intent) {
				is ToSettingsIntent -> toSettings()
			}
		},
		onBreedsAction = viewModel::onAction
	)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedsListContent(
	state: BreedsListUiState,
	onScreenAction: (ScreenIntent) -> Unit,
	onBreedsAction: (BreedsIntent) -> Unit
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(text = stringResource(id = R.string.breeds))
				},
				actions = {
					var expandDropdownMenu by rememberSaveable { mutableStateOf(false) }

					IconButton(onClick = { expandDropdownMenu = !expandDropdownMenu }) {
						Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
					}

					DropdownMenu(
						expanded = expandDropdownMenu,
						offset = DpOffset(x = (-8).dp, y = 0.dp),
						onDismissRequest = { expandDropdownMenu = false }
					) {
						DropdownMenuItem(
							text = { Text(text = stringResource(id = R.string.settings)) },
							onClick = {
								expandDropdownMenu = false
								onScreenAction(ToSettingsIntent)
							}
						)
					}
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				state = rememberSaveable(
					inputs = arrayOf(state.breeds.getOrNull(0)),
					saver = LazyListState.Saver
				) { LazyListState() },
				contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				items(
					count = state.breeds.size,
					key = { index -> state.breeds[index].hash }
				) { index ->
					val breedUiState = state.breeds[index]
					BreedCard(
						modifier = Modifier
							.animateItemPlacement(),
						state = breedUiState,
						strings = BreedCardDefault.strings(
							origin = stringResource(id = R.string.origin),
							coat = stringResource(id = R.string.coat),
							pattern = stringResource(id = R.string.pattern),
							country = stringResource(id = R.string.country)
						),
						onChangeExpand = { onBreedsAction(OnChangeExpandIntent(breedUiState.hash)) },
						onChangeFavourite = {
							onBreedsAction(OnChangeFavouriteIntent(breedUiState.hash))
						}
					)

					SideEffect {
						if (index == state.breeds.size - 1) {
							onBreedsAction(OnAppendContentIntent)
						}
					}
				}

				item {
					AnimatedVisibility(
						modifier = Modifier
							.fillMaxWidth()
							.animateItemPlacement(),
						enter = fadeIn() + expandVertically(),
						exit = fadeOut() + shrinkVertically(),
						visible = !state.isNoMore
					) {
						Box {
							val color = if (state.isLoading) MaterialTheme.colorScheme.surface
							else MaterialTheme.colorScheme.primary
							val animatedColor by animateColorAsState(
								targetValue = color,
								label = "progressColor"
							)
							CircularProgressIndicator(
								modifier = Modifier
									.align(Alignment.Center)
									.padding(top = 16.dp),
								color = animatedColor
							)
						}
					}
				}
			}

			AnimatedVisibility(
				modifier = Modifier.align(Alignment.Center),
				enter = fadeIn(),
				exit = fadeOut(),
				visible = state.isLoading
			) {
				CircularProgressIndicator()
			}

			AnimatedVisibility(
				modifier = Modifier
					.align(Alignment.BottomCenter),
				enter = slideInVertically { it },
				exit = slideOutVertically { it },
				visible = state.isNoConnection
			) {
				ErrorPanel(
					state = ErrorState(
						title = stringResource(id = R.string.no_connection),
						callbackLabel = stringResource(id = R.string.refresh)
					),
					enabledCallback = !state.isLoading,
					onCallback = { onBreedsAction(OnRefreshIntent) }
				)
			}
		}
	}
}

@Preview
@Composable
private fun PreviewBreedsListContent() {
	CatNFactTheme {
		Surface {
			BreedsListContent(
				state = BreedsListUiState(
					breeds = listOf(
						BreedUiState(hash = "1"),
						BreedUiState(hash = "2"),
						BreedUiState(hash = "3")
					),
					isLoading = true,
					isNoConnection = true
				),
				onScreenAction = {},
				onBreedsAction = {}
			)
		}
	}
}
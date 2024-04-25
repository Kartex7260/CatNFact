package kanti.catnfact.feat.facts.screens.factslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kanti.catnfact.feat.facts.R
import kanti.catnfact.ui.components.error.ErrorPanel
import kanti.catnfact.ui.components.error.ErrorState
import kanti.catnfact.ui.components.fact.FactCard
import kanti.catnfact.ui.components.fact.FactUiState
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FactsListScreen(
	onBack: () -> Unit = {},
	toSettings: () -> Unit = {}
) {
	val viewModel = hiltViewModel<FactsListViewModel>()
	val uiState by viewModel.factsListUiState.collectAsState()

	FactsListContent(
		state = uiState,
		onScreenAction = { intent ->
			when (intent) {
				is OnBackIntent -> onBack()
				is ToSettingsIntent -> toSettings()
			}
		},
		onFactAction = viewModel::onFactAction
	)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FactsListContent(
	state: FactsListUiState,
	onScreenAction: (ScreenIntent) -> Unit,
	onFactAction: (FactIntent) -> Unit
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text(text = stringResource(id = R.string.facts)) },
				navigationIcon = {
					IconButton(onClick = { onScreenAction(OnBackIntent) }) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = null
						)
					}
				},
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
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { paddingValues ->
		Box(modifier = Modifier.padding(paddingValues)) {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
			) {
				items(
					count = state.facts.size,
					key = { state.facts[it].hash }
				) { index ->
					val factUiState = state.facts[index]
					FactCard(
						modifier = Modifier.animateItemPlacement(),
						state = factUiState,
						onChangeExpand = { onFactAction(OnChangeExpandIntent(factUiState.hash)) },
						onChangeFavourite = { onFactAction(ChangeFavouriteIntent(factUiState.hash)) }
					)

					if (index == state.facts.size - 1) {
						onFactAction(AppendContentIntent)
					}
				}

				if (!state.isLast) {
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 16.dp),
							contentAlignment = Alignment.Center
						) {
							CircularProgressIndicator()
						}
					}
				}
			}

			if (state.isLoading) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center)
				)
			}

			if (state.isNoConnection) {
				ErrorPanel(
					modifier = Modifier.align(Alignment.BottomCenter),
					state = ErrorState(
						title = stringResource(id = R.string.no_connection),
						callbackLabel = stringResource(id = R.string.refresh)
					),
					onCallback = { onFactAction(RefreshIntent) }
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewFactsListContent() {
	CatNFactTheme {
		Surface {
			FactsListContent(
				state = FactsListUiState(
					facts = listOf(
						FactUiState(
							hash = "123",
							fact = "Fact fact fact fact fact",
							isFavourite = false,
							isExpand = true
						),
						FactUiState(
							hash = "1234",
							fact = "Fact fact fact fact fact",
							isFavourite = false,
							isExpand = false
						),
						FactUiState(
							hash = "12345",
							fact = "Fact fact fact fact fact",
							isFavourite = true,
							isExpand = false
						)
					),
					isLoading = true,
					isNoConnection = true
				),
				onScreenAction = {},
				onFactAction = {}
			)
		}
	}
}
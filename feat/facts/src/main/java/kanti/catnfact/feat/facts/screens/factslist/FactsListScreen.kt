package kanti.catnfact.feat.facts.screens.factslist

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
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
	val state by viewModel.factsListUiState.collectAsState()

	LifecycleStartEffect(viewModel) {
		if (state.isNoConnection)
			viewModel.onFactAction(OnRefreshIntent)
		else
			viewModel.updateData()
		onStopOrDispose { }
	}

	FactsListContent(
		state = state,
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
						onDismissRequest = { expandDropdownMenu = false },
						offset = DpOffset(x = (-8).dp, y = 0.dp)
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
								expandDropdownMenu = false
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
				state = rememberSaveable(
					inputs = arrayOf(state.facts.getOrNull(0)),
					saver = LazyListState.Saver
				) { LazyListState(0, 0) },
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
						onChangeFavourite = { onFactAction(OnChangeFavouriteIntent(factUiState.hash)) }
					)

					SideEffect {
						if (index == state.facts.size - 1) {
							onFactAction(OnAppendContentIntent)
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
				visible = state.isLoading,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				CircularProgressIndicator()
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
					enabledCallback = !state.isLoading,
					onCallback = { onFactAction(OnRefreshIntent) }
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
package kanti.catnfact.feat.favourite.screens.breedslist

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import kanti.catnfact.feat.favourite.R
import kanti.catnfact.ui.components.breed.BreedCard
import kanti.catnfact.ui.components.breed.BreedCardDefault
import kanti.catnfact.ui.components.breed.BreedUiState
import kanti.catnfact.ui.components.error.ErrorPanel
import kanti.catnfact.ui.components.error.ErrorState
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FavouriteBreedsListScreen(
	modifier: Modifier = Modifier
) {
	val viewModel = hiltViewModel<FavouriteBreedsListViewModel>()
	val state by viewModel.breedsUiState.collectAsState()

	LifecycleStartEffect {
		viewModel.onScreenAction(OnStartIntent)
		onStopOrDispose {  }
	}

	FavouriteBreedsListContent(
		modifier = modifier,
		state = state,
		onBreedAction = viewModel::onBreedAction
	)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteBreedsListContent(
	modifier: Modifier = Modifier,
	state: FavouriteBreedsListUiState,
	onBreedAction: (BreedIntent) -> Unit
) {
	Box(modifier = modifier) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
		) {
			items(
				count = state.breeds.size,
				key = { index -> state.breeds[index].hash }
			) { index ->
				val breedUiState = state.breeds[index]
				BreedCard(
					modifier = Modifier
						.animateItemPlacement(),
					strings = BreedCardDefault.strings(
						origin = stringResource(id = R.string.origin),
						coat = stringResource(id = R.string.coat),
						pattern = stringResource(id = R.string.pattern),
						country = stringResource(id = R.string.country)
					),
					state = breedUiState,
					onChangeExpand = { onBreedAction(OnChangeExpandIntent(breedUiState.hash)) },
					onChangeFavourite = { onBreedAction(OnChangeFavouriteIntent(breedUiState.hash)) }
				)

				SideEffect {
					if (index == state.breeds.size - 1) {
						onBreedAction(OnAppendContentIntent)
					}
				}
			}

			item {
				AnimatedVisibility(
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(),
					visible = !state.isNoMore,
					enter = fadeIn() + expandVertically(),
					exit = fadeOut() + shrinkVertically()
				) {
					Box {
						val color = if (state.isLoading) MaterialTheme.colorScheme.surface
						else MaterialTheme.colorScheme.primary
						val animatedColor by animateColorAsState(targetValue = color, label = "progressColor")
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
				onCallback = { onBreedAction(OnRefreshIntent) }
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewFavouriteBreedsListContent() {
	CatNFactTheme {
		Surface {
			FavouriteBreedsListContent(
				state = FavouriteBreedsListUiState(
					breeds = listOf(
						BreedUiState(
							hash = "1",
							breed = "Breed"
						),
						BreedUiState(
							hash = "2",
							breed = "Breed"
						),
						BreedUiState(
							hash = "3",
							breed = "Breed"
						),
						BreedUiState(
							hash = "4",
							breed = "Breed"
						)
					),
					isLoading = true,
					isNoConnection = true,
					isNoMore = false
				),
				onBreedAction = {}
			)
		}
	}
}
package kanti.catnfact.feat.favourite.screens.factslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanti.catnfact.feat.favourite.R
import kanti.catnfact.ui.components.error.ErrorPanel
import kanti.catnfact.ui.components.error.ErrorState
import kanti.catnfact.ui.components.fact.FactCard
import kanti.catnfact.ui.components.fact.FactUiState
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FavouriteFactsListScreen(
	modifier: Modifier = Modifier
) {
	FavouriteFactsListContent(
		modifier = modifier,
		state = FavouriteFactsListUiState(),
		onFactAction = {}
	)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteFactsListContent(
	modifier: Modifier = Modifier,
	state: FavouriteFactsListUiState,
	onFactAction: (FactIntent) -> Unit
) {
	Box(
		modifier = modifier
	) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
		) {
			items(
				count = state.facts.size
			) { index ->
				val factUiState = state.facts[index]
				FactCard(
					modifier = Modifier.animateItemPlacement(),
					state = factUiState,
					onChangeFavourite = { onFactAction(OnChangeFavouriteIntent(factUiState.hash)) },
					onChangeExpand = { onFactAction(OnChangeExpandIntent(factUiState.hash)) }
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
					visible = !state.isNoMore,
					enter = fadeIn() + expandVertically(),
					exit = fadeOut() + shrinkVertically()
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
			enter = expandVertically { it },
			exit = shrinkVertically { it }
		) {
			ErrorPanel(
				state = ErrorState(
					title = stringResource(id = R.string.no_connection),
					callbackLabel = stringResource(id = R.string.refresh)
				),
				onCallback = { onFactAction(OnRefreshIntent) }
			)
		}
	}
}

@Preview
@Composable
private fun PreviewFavouriteFactsListContent() {
	CatNFactTheme {
		Surface {
			FavouriteFactsListContent(
				state = FavouriteFactsListUiState(
					facts = listOf(
						FactUiState(hash = "1", fact = "Test"),
						FactUiState(hash = "2", fact = "Test"),
						FactUiState(hash = "3", fact = "Test"),
						FactUiState(hash = "4", fact = "Test")
					),
					isLoading = true,
					isNoConnection = true,
					isNoMore = false
				),
				onFactAction = {}
			)
		}
	}
}
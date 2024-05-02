package kanti.catnfact.feat.favourite.screens.factslist

import kanti.catnfact.ui.components.fact.FactUiState

data class FavouriteFactsListUiState(
	val facts: List<FactUiState> = listOf(),
	val isLoading: Boolean = false,
	val isNoConnection: Boolean = false,
	val isNoMore: Boolean = true
)
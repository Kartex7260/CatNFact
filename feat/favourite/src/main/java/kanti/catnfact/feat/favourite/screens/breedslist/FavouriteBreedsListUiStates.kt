package kanti.catnfact.feat.favourite.screens.breedslist

import kanti.catnfact.ui.components.breed.BreedUiState

data class FavouriteBreedsListUiState(
	val breeds: List<BreedUiState> = listOf(),
	val isLoading: Boolean = false,
	val isNoConnection: Boolean = false,
	val isNoMore: Boolean = true
)
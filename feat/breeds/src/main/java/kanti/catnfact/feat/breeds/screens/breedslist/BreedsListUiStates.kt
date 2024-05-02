package kanti.catnfact.feat.breeds.screens.breedslist

import androidx.compose.runtime.Immutable
import kanti.catnfact.ui.components.breed.BreedUiState

@Immutable
data class BreedsListUiState(
	val breeds: List<BreedUiState> = listOf(),
	val isLoading: Boolean = false,
	val isNoConnection: Boolean = false,
	val isNoMore: Boolean = true
)
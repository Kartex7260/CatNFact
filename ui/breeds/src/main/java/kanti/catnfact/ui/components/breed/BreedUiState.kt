package kanti.catnfact.ui.components.breed

import androidx.compose.runtime.Immutable

@Immutable
data class BreedUiState(
	val hash: String = "",

	val breed: String = "",
	val country: String = "",
	val origin: String = "",
	val coat: String = "",
	val pattern: String = "",

	val isFavourite: Boolean = false,
	val isExpand: Boolean = false
)

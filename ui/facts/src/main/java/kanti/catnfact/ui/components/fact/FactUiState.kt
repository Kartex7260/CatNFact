package kanti.catnfact.ui.components.fact

import androidx.compose.runtime.Immutable

@Immutable
data class FactUiState(
	val hash: String = "",
	val fact: String = "",
	val isFavourite: Boolean = false
)

package kanti.catnfact.feat.facts.screens.factslist

import androidx.compose.runtime.Immutable
import kanti.catnfact.ui.components.fact.FactUiState

@Immutable
data class FactsListUiState(
	val facts: List<FactUiState> = listOf(),
	val isLoading: Boolean = false,
	val isNoConnection: Boolean = false,
	val isLast: Boolean = false
)

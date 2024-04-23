package kanti.catnfact.feat.facts.screens.randomfact

import androidx.compose.runtime.Immutable
import kanti.catnfact.ui.components.fact.FactUiState

@Immutable
data class RandomFactUiState(
	val fact: FactUiState = FactUiState(),
)
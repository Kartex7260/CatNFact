package kanti.catnfact.feat.facts

import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.ui.components.fact.FactUiState

fun Fact.toUiState(): FactUiState {
	return FactUiState(
		hash = hash,
		fact = fact,
		isFavourite = isFavourite
	)
}
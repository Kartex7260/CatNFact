package kanti.catnfact.feat.favourite

import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.ui.components.fact.FactUiState

fun Fact.toUiState(isExpand: Boolean): FactUiState {
	return FactUiState(
		hash = hash,
		fact = fact,
		isFavourite = isFavourite,
		isExpand = isExpand
	)
}
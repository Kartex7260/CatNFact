package kanti.catnfact.feat.favourite

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.ui.components.breed.BreedUiState
import kanti.catnfact.ui.components.fact.FactUiState

fun Fact.toUiState(isExpand: Boolean): FactUiState {
	return FactUiState(
		hash = hash,
		fact = fact,
		isFavourite = isFavourite,
		isExpand = isExpand
	)
}

fun Breed.toUiState(isExpand: Boolean): BreedUiState {
	return BreedUiState(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern,

		isFavourite = isFavourite,
		isExpand = isExpand
	)
}
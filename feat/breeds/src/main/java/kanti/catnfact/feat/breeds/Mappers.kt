package kanti.catnfact.feat.breeds

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.ui.components.breed.BreedUiState

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
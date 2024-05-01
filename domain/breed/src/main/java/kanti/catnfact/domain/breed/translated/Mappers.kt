package kanti.catnfact.domain.breed.translated

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.translated.BreedData

fun Breed.toTranslated(data: BreedData?): Breed {
	return Breed(
		hash = hash,

		breed = data?.breed ?: breed,
		country = data?.country ?: country,
		origin = data?.origin ?: origin,
		coat = data?.coat ?: coat,
		pattern = data?.pattern ?: pattern,

		isFavourite = isFavourite
	)
}

fun Breed.toData(): BreedData {
	return BreedData(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern
	)
}
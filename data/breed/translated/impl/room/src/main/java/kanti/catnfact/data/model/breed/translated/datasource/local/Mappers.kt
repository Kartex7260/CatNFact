package kanti.catnfact.data.model.breed.translated.datasource.local

import kanti.catnfact.data.model.breed.translated.BreedData
import kanti.catnfact.data.room.breed.translated.TranslatedBreedEntity

fun TranslatedBreedEntity.toBreed(): BreedData {
	return BreedData(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern
	)
}

fun BreedData.toEntity(
	fromLocaleCode: String,
	targetLocaleCode: String
): TranslatedBreedEntity {
	return TranslatedBreedEntity(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern,

		fromLocaleCode = fromLocaleCode,
		targetLocaleCode = targetLocaleCode
	)
}
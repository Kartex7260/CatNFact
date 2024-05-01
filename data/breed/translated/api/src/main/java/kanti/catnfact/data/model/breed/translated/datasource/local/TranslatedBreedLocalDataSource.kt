package kanti.catnfact.data.model.breed.translated.datasource.local

import kanti.catnfact.data.model.breed.translated.BreedData

interface TranslatedBreedLocalDataSource {

	suspend fun getUntranslated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): List<BreedData>

	suspend fun getBreeds(
		hashes: List<String>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): List<BreedData>

	suspend fun insert(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	)
}
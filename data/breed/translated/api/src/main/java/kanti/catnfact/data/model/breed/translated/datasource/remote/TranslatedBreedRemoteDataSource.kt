package kanti.catnfact.data.model.breed.translated.datasource.remote

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.breed.translated.BreedData

interface TranslatedBreedRemoteDataSource {

	suspend fun translated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): DataResult<List<BreedData>, DataError>
}
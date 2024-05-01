package kanti.catnfact.data.model.breed.translated

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult

interface TranslatedBreedRepository {

	suspend fun translate(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): DataResult<List<BreedData>, DataError>
}
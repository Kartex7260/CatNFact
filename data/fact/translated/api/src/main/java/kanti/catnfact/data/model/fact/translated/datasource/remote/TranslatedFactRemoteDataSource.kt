package kanti.catnfact.data.model.fact.translated.datasource.remote

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact

interface TranslatedFactRemoteDataSource {

	suspend fun translate(
		facts: List<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): DataResult<List<TranslatedFact>, DataError>
}
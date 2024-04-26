package kanti.catnfact.data.model.fact.translated

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact

interface TranslatedFactRepository {

	suspend fun translate(
		facts: Sequence<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): DataResult<List<TranslatedFact>, DataError>
}
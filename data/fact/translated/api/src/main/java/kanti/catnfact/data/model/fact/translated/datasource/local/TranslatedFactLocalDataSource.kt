package kanti.catnfact.data.model.fact.translated.datasource.local

import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact

interface TranslatedFactLocalDataSource {

	suspend fun getTranslatedFacts(
		facts: Sequence<String>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): List<TranslatedFact>

	suspend fun returnUntranslated(
		facts: Sequence<String>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): List<HalfFact>

	suspend fun insert(facts: List<TranslatedFact>)
}
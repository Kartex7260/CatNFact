package kanti.catnfact.data.model.fact.translated.datasource.local

import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.room.fact.translated.TranslatedFactEntity

fun TranslatedFactEntity.toTranslatedFact(): TranslatedFact {
	return TranslatedFact(
		hash = hash,
		fact = fact,
		fromLocaleCode = fromLocaleCode,
		destinationLocaleCode = destinationLocaleCode
	)
}

fun TranslatedFact.toEntity(): TranslatedFactEntity {
	return TranslatedFactEntity(
		hash = hash,
		fact = fact,
		fromLocaleCode = fromLocaleCode,
		destinationLocaleCode = destinationLocaleCode
	)
}
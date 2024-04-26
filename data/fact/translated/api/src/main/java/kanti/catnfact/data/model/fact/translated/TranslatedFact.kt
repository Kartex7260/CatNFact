package kanti.catnfact.data.model.fact.translated

data class TranslatedFact(
	val hash: String = "",
	val fact: String = "",
	val fromLocaleCode: String = "",
	val destinationLocaleCode: String = ""
)

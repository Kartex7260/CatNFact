package kanti.catnfact.data.retrofit.ya.translator

data class Translations(
	val translations: List<Translated> = listOf()
)

data class Translated(
	val text: String = ""
)

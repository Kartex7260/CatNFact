package kanti.catnfact.data.retrofit.libretranslate.translate

import com.google.gson.annotations.SerializedName

data class TranslateBody(
	@SerializedName("q") val text: String = "",
	@SerializedName("source") val sourceLocaleCode: String = "",
	@SerializedName("target") val targetLocaleCode: String = "",
	val format: String = FORMAT_TEXT,
	@SerializedName("api_key") val apiKey: String = ""
) {

	companion object {

		const val FORMAT_TEXT = "text"
		const val FORMAT_HTML = "html"
	}
}

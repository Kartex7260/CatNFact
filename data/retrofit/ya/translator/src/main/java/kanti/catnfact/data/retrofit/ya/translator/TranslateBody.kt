package kanti.catnfact.data.retrofit.ya.translator

data class TranslateBody(
	val sourceLanguageCode: String,
	val targetLanguageCode: String,
	val texts: List<String>,

	val folderId: String = BuildConfig.YA_CLOUD_FOLDER_ID,
	val format: String = FORMAT_PLAIN_TEXT,
	val speller: Boolean = false
) {

	companion object {

		const val FORMAT_PLAIN_TEXT = "PLAIN_TEXT"
		const val FORMAT_HTML = "HTML"
	}
}

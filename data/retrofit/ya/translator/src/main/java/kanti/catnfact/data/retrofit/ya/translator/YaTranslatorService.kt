package kanti.catnfact.data.retrofit.ya.translator

import retrofit2.http.Body
import retrofit2.http.POST

interface YaTranslatorService {

	@POST("translate")
	suspend fun translate(@Body translateBody: TranslateBody): Translations
}
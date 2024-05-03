package kanti.catnfact.data.retrofit.libretranslate.translate

import retrofit2.http.Body
import retrofit2.http.POST

interface LibreTranslateService {

	@POST("translate")
	suspend fun translate(@Body body: TranslateBody): TranslatedResponse
}
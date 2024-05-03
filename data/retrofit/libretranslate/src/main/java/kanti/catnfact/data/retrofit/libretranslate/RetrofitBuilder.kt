package kanti.catnfact.data.retrofit.libretranslate

import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import retrofit2.Retrofit

private const val baseUrl = "https://libretranslate.com/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.build()

fun buildLibreTranslateService(retrofit: Retrofit): LibreTranslateService = retrofit
	.create(LibreTranslateService::class.java)
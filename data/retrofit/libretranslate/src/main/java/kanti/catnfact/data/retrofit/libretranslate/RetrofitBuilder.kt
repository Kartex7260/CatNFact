package kanti.catnfact.data.retrofit.libretranslate

import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://libretranslate.com/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.addConverterFactory(GsonConverterFactory.create())
	.build()

fun buildLibreTranslateService(retrofit: Retrofit): LibreTranslateService = retrofit
	.create(LibreTranslateService::class.java)
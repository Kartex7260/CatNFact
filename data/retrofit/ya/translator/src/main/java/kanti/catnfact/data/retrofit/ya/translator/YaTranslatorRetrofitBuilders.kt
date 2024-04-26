package kanti.catnfact.data.retrofit.ya.translator

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://translate.api.cloud.yandex.net/translate/v2/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.addConverterFactory(GsonConverterFactory.create())
	.client(buildHttpClient())
	.build()

fun buildHttpClient(): OkHttpClient = OkHttpClient.Builder()
	.addInterceptor(apiKeyInterceptor)
	.build()

val apiKeyInterceptor = Interceptor {
	val request = it.request().newBuilder()
		.addHeader("Authorization", "Api-Key ${BuildConfig.YA_TRANSLATOR_API_KEY}")
		.build()
	it.proceed(request)
}

fun buildYaTranslatorService(retrofit: Retrofit): YaTranslatorService = retrofit
	.create(YaTranslatorService::class.java)
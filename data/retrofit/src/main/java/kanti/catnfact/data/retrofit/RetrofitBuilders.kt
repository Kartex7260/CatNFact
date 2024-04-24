package kanti.catnfact.data.retrofit

import kanti.catnfact.data.retrofit.fact.FactService
import retrofit2.Retrofit

private const val baseUrl = "https://catfact.ninja/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.build()

fun buildFactService(retrofit: Retrofit): FactService = retrofit
	.create(FactService::class.java)
package kanti.catnfact.data.retrofit

import kanti.catnfact.data.retrofit.fact.FactService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://catfact.ninja/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.addConverterFactory(GsonConverterFactory.create())
	.build()

fun buildFactService(retrofit: Retrofit): FactService = retrofit
	.create(FactService::class.java)
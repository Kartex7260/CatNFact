package kanti.catnfact.data.retrofit.catfact

import kanti.catnfact.data.retrofit.catfact.breed.BreedService
import kanti.catnfact.data.retrofit.catfact.fact.FactService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://catfact.ninja/"

fun buildRetrofit(): Retrofit = Retrofit.Builder()
	.baseUrl(baseUrl)
	.addConverterFactory(GsonConverterFactory.create())
	.build()

fun buildFactService(retrofit: Retrofit): FactService = retrofit
	.create(FactService::class.java)

fun buildBreedService(retrofit: Retrofit): BreedService = retrofit
	.create(BreedService::class.java)
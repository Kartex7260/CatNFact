package kanti.catnfact.data.retrofit.fact

import retrofit2.http.GET

interface FactService {

	@GET("fact")
	suspend fun getRandomFact(): FactDto
}
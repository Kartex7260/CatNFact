package kanti.catnfact.data.retrofit.catfact.fact

import retrofit2.http.GET
import retrofit2.http.Query

interface FactService {

	@GET("fact")
	suspend fun getRandomFact(): FactDto

	@GET("facts")
	suspend fun getFactsList(@Query("page") page: Int, @Query("limit") limit: Int): FactsDto
}
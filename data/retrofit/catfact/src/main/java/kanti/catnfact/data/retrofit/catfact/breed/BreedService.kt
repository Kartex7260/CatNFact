package kanti.catnfact.data.retrofit.catfact.breed

import retrofit2.http.GET
import retrofit2.http.Query

interface BreedService {

	@GET("breeds")
	suspend fun getBreeds(@Query("page") page: Int, @Query("limit") limit: Int): BreedsDto
}
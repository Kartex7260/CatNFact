package kanti.catnfact.data.model.breed.datasource.remote

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.breed.Breed

interface BreedRemoteDataSource {

	suspend fun getBreeds(page: Int, limit: Int): DataResult<List<Breed>, DataError>
}
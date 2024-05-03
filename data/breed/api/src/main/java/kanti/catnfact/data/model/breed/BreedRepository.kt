package kanti.catnfact.data.model.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult

interface BreedRepository {

	suspend fun getLocalFavouriteBreedHashes(page: Int, limit: Int): List<String>


	suspend fun changeFavourite(hash: String)


	suspend fun getLocalBreedsHashes(limit: Int): List<String>

	suspend fun loadFacts(page: Int, limit: Int): DataResult<List<String>, DataError>

	suspend fun getLocalBreeds(hashes: List<String>): List<Breed>
}
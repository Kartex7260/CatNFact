package kanti.catnfact.data.model.breed.datasource.local

import kanti.catnfact.data.model.breed.Breed

interface BreedLocalDataSource {

	suspend fun changeFavourite(hash: String)

	suspend fun getBreedsHashes(limit: Int): List<String>

	suspend fun getBreeds(hashes: List<String>): List<Breed>

	suspend fun insert(breeds: List<Breed>)
}
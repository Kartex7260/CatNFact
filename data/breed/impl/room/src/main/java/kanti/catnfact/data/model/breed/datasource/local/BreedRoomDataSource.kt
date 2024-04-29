package kanti.catnfact.data.model.breed.datasource.local

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.room.breed.BreedDao
import javax.inject.Inject

class BreedRoomDataSource @Inject constructor(
	private val breedDao: BreedDao
) : BreedLocalDataSource {

	override suspend fun changeFavourite(hash: String) {
		breedDao.changeFavourite(hash = hash)
	}

	override suspend fun getBreedsHashes(limit: Int): List<String> {
		return breedDao.getBreedsHashes(limit = limit)
	}

	override suspend fun getBreeds(hashes: List<String>): List<Breed> {
		return breedDao.getBreeds(hashes = hashes).map { it.toBreed() }
	}

	override suspend fun insert(breeds: List<Breed>) {
		val dbBreeds = breedDao.getBreeds(hashes = breeds.map { it.hash })
		breedDao.insert(
			breeds = breeds.map { breed ->
				val dbBreed = dbBreeds.firstOrNull { it.hash == breed.hash }
				breed.toEntity(isFavourite = dbBreed?.isFavourite ?: false)
			}
		)
	}
}
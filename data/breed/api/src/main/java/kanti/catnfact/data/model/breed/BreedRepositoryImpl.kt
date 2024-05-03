package kanti.catnfact.data.model.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.breed.datasource.local.BreedLocalDataSource
import kanti.catnfact.data.model.breed.datasource.remote.BreedRemoteDataSource
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
	private val localDataSource: BreedLocalDataSource,
	private val remoteDataSource: BreedRemoteDataSource
) : BreedRepository {

	override suspend fun getLocalFavouriteBreedHashes(page: Int, limit: Int): List<String> {
		return localDataSource.getFavouriteBreedHashes(page = page, limit = limit)
	}

	override suspend fun changeFavourite(hash: String) {
		localDataSource.changeFavourite(hash)
	}

	override suspend fun getLocalBreedsHashes(limit: Int): List<String> {
		return localDataSource.getBreedsHashes(limit = limit)
	}

	override suspend fun loadFacts(page: Int, limit: Int): DataResult<List<String>, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteResult = remoteDataSource.getBreeds(page = page, limit = limit)
			remoteResult.runIfNotError { breeds ->
				localDataSource.insert(breeds)
				DataResult.Success(breeds.map { it.hash })
			}
		}
	}

	override suspend fun getLocalBreeds(hashes: List<String>): List<Breed> {
		return localDataSource.getBreeds(hashes = hashes)
	}
}
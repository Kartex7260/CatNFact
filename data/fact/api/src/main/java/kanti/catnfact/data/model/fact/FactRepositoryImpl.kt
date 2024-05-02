package kanti.catnfact.data.model.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.LocalError
import kanti.catnfact.data.model.fact.datasource.local.FactLocalDataSource
import kanti.catnfact.data.model.fact.datasource.remote.FactRemoteDataSource
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FactRepositoryImpl @Inject constructor(
	private val localDataSource: FactLocalDataSource,
	private val remoteDataSource: FactRemoteDataSource
) : FactRepository {

	override suspend fun localFavouritesFacts(page: Int, limit: Int): List<String> {
		return localDataSource.loadFavouriteFacts(page = page, limit = limit)
	}

	override suspend fun getFact(hash: String): DataResult<Fact, LocalError> {
		return localDataSource.getFact(hash)
	}

	override suspend fun getRandomFact(): DataResult<Fact, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteFact = remoteDataSource.getRandomFact()
			remoteFact.runIfNotError { fact ->
				localDataSource.insert(fact)
				localDataSource.getFact(fact.hash).runIfNotError { localFact ->
					DataResult.Success(localFact)
				}
			}
		}
	}

	override suspend fun changeFavourite(hash: String): DataResult<Fact, DataError> {
		return withContext(Dispatchers.Default) {
			localDataSource.changeFavourite(hash)
			val factResult = localDataSource.getFact(hash)

			factResult.runIfNotError {
				DataResult.Success(it)
			}
		}
	}

	override suspend fun getLocalFactsHashes(limit: Int): List<String> {
		return localDataSource.getFactsHashes(limit = limit)
	}

	override suspend fun getLocalFacts(hashes: List<String>): List<Fact> {
		return localDataSource.getFacts(hashes = hashes)
	}

	override suspend fun loadFacts(page: Int, limit: Int): DataResult<List<String>, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteResult = remoteDataSource.getFactsList(page = page, limit = limit)
			remoteResult.runIfNotError { facts ->
				facts.forEach { localDataSource.insert(it) }
				DataResult.Success(value = facts.map { it.hash })
			}
		}
	}
}
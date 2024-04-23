package kanti.catnfact.data.model.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.fact.datasource.local.FactLocalDataSource
import kanti.catnfact.data.model.fact.datasource.remote.FactRemoteDataSource
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FactRepositoryImpl @Inject constructor(
	private val localDataSource: FactLocalDataSource,
	private val remoteDataSource: FactRemoteDataSource
) : FactRepository {

	override suspend fun getRandomFact(): DataResult<Fact, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteFact = remoteDataSource.getRandomFact()

			val error = remoteFact.error
			if (error != null) {
				val localFact = localDataSource.getRandomFact()
				return@withContext DataResult.Error(error, localFact.value)
			}

			remoteFact.runIfNotError { fact ->
				launch {
					localDataSource.insert(fact)
				}
				DataResult.Success(fact)
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
}
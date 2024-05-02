package kanti.catnfact.data.model.fact.datasource.local

import kanti.catnfact.data.DataResult
import kanti.catnfact.data.LocalError
import kanti.catnfact.data.NotFoundError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.room.fact.FactDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FactRoomDataSource @Inject constructor(
	private val factDao: FactDao
) : FactLocalDataSource {

	override suspend fun loadFavouriteFacts(page: Int, limit: Int): List<String> {
		val offset = (page - 1) * limit
		return factDao.getHashes(limit = limit, offset = offset)
	}

	override suspend fun getFact(hash: String): DataResult<Fact, LocalError> {
		return withContext(Dispatchers.Default) {
			val fact = factDao.getFact(hash)
			if (fact == null)
				DataResult.Error(NotFoundError())
			else
				DataResult.Success(fact.toFact())
		}
	}

	override suspend fun insert(fact: Fact) {
		withContext(Dispatchers.Default) {
			val factFromDb = factDao.getFact(fact.hash)
			factDao.insert(fact.toEntity(factFromDb?.isFavourite))
		}
	}

	override suspend fun changeFavourite(hash: String) {
		factDao.changeFavourite(hash)
	}


	override suspend fun getFactsHashes(limit: Int): List<String> {
		return factDao.getAllHashes(limit = limit)
	}

	override suspend fun getFacts(hashes: List<String>): List<Fact> {
		return factDao.getFactsList(hashes = hashes).map { it.toFact() }
	}
}
package kanti.catnfact.data.model.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.LocalError
import kanti.catnfact.data.RemoteError

interface FactRepository {

	suspend fun getFact(hash: String): DataResult<Fact, LocalError>

	suspend fun getRandomFact(): DataResult<Fact, DataError>

	suspend fun changeFavourite(hash: String): DataResult<Fact, DataError>


	suspend fun getLocalFactsHashes(limit: Int): List<String>

	suspend fun getLocalFacts(hashes: List<String>): List<Fact>

	suspend fun loadFacts(page: Int, limit: Int): DataResult<List<String>, RemoteError>
}
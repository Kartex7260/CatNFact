package kanti.catnfact.data.model.fact.datasource.local

import kanti.catnfact.data.DataResult
import kanti.catnfact.data.LocalError
import kanti.catnfact.data.model.fact.Fact

interface FactLocalDataSource {

	suspend fun getRandomFact(): DataResult<Fact, LocalError>

	suspend fun getFact(hash: String): DataResult<Fact, LocalError>

	suspend fun insert(fact: Fact)

	suspend fun changeFavourite(hash: String)


	suspend fun getFactsHashes(limit: Int): List<String>

	suspend fun getFacts(hashes: List<String>): List<Fact>
}
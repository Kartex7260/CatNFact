package kanti.catnfact.data.model.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.LocalError

interface FactRepository {

	suspend fun getFact(hash: String): DataResult<Fact, LocalError>

	suspend fun getRandomFact(): DataResult<Fact, DataError>

	suspend fun changeFavourite(hash: String): DataResult<Fact, DataError>
}
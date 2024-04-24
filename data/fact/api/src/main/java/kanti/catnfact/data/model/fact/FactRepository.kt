package kanti.catnfact.data.model.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult

interface FactRepository {

	suspend fun getRandomFact(): DataResult<Fact, DataError>

	suspend fun changeFavourite(hash: String): DataResult<Fact, DataError>
}
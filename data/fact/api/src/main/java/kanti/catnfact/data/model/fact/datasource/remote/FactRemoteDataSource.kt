package kanti.catnfact.data.model.fact.datasource.remote

import kanti.catnfact.data.DataResult
import kanti.catnfact.data.RemoteError
import kanti.catnfact.data.model.fact.Fact

interface FactRemoteDataSource {

	suspend fun getRandomFact(): DataResult<Fact, RemoteError>
}
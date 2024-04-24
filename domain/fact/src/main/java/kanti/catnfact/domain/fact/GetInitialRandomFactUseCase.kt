package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.app.AppDataRepository
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetInitialRandomFactUseCase @Inject constructor(
	private val appDataRepository: AppDataRepository,
	private val factRepository: FactRepository,
	private val getRandomFactUseCase: GetRandomFactUseCase
) {

	suspend operator fun invoke(): Flow<DataResult<Fact, DataError>> {
		return channelFlow {
			withContext(Dispatchers.Default) {
				val lastHash = appDataRepository.lastFactHash.firstOrNull()
				if (lastHash != null) {
					val fact = factRepository.getFact(lastHash)
					send(fact.runIfNotError { DataResult.Success(it) })
				}

				val randomFact = getRandomFactUseCase()
				send(randomFact)
			}
		}
	}
}
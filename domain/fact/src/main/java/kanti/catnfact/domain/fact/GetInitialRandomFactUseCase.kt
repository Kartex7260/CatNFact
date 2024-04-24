package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.app.AppDataRepository
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
				val localJob = launch {
					val lastHash = appDataRepository.lastFactHash.first() ?: return@launch
					val fact = factRepository.getFact(lastHash).value ?: return@launch
					send(DataResult.Success(fact))
				}

				val randomFact = getRandomFactUseCase()
				localJob.cancel()
				send(randomFact)
			}
		}
	}
}
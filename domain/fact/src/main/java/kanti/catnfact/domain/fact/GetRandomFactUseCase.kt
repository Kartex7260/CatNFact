package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.app.AppDataRepository
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomFactUseCase @Inject constructor(
	private val appDataRepository: AppDataRepository,
	private val factRepository: FactRepository
) {

	suspend operator fun invoke(): DataResult<Fact, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteFact = factRepository.getRandomFact()

			remoteFact.runIfNotError { fact ->
				launch { appDataRepository.setLastFactHash(fact.hash) }
				DataResult.Success(fact)
			}
		}
	}
}
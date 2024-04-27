package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.app.AppDataRepository
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomFactUseCase @Inject constructor(
	private val appDataRepository: AppDataRepository,
	private val factRepository: FactRepository,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) {

	suspend operator fun invoke(): DataResult<Fact, DataError> {
		return withContext(Dispatchers.Default) {
			val remoteFact = factRepository.getRandomFact()

			remoteFact.runIfNotError { fact ->
				launch { appDataRepository.setLastFactHash(fact.hash) }
				val translatedFact = getTranslatedFactsUseCase(listOf(fact.hash))
				translatedFact.runIfNotError { DataResult.Success(it[0]) }
			}
		}
	}
}
package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FactsPagingManager @Inject constructor(
	private val factRepository: FactRepository,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) : Pagination<Fact> {

	private val pagination = object : BasePagination<String>() {

		override suspend fun loadLocal0(limit: Int): List<String> {
			return factRepository.getLocalFactsHashes(limit = limit)
		}

		override suspend fun load0(page: Int, limit: Int): DataResult<List<String>, DataError> {
			return factRepository.loadFacts(page = page, limit = limit)
		}
	}

	override val isNoMore: StateFlow<Boolean> = pagination.isNoMore

	override suspend fun getData(): DataResult<List<Fact>, DataError> {
		val paginatedData = pagination.getData()
		val error = paginatedData.error
		val paginatedHashes = paginatedData.value

		return if (error != null) {
			DataResult.Error(
				error = error,
				value = paginatedHashes?.let { hashes ->
					val translatedFacts = getTranslatedFactsUseCase(hashes).value ?: return@let null
					hashes.map { hash -> translatedFacts.first { it.hash == hash } }
				}
			)
		} else if (paginatedHashes != null) {
			val translatedFacts = getTranslatedFactsUseCase(paginatedHashes)
			translatedFacts.runIfNotError { facts ->
				DataResult.Success(
					value = paginatedHashes.map { hash -> facts.first { it.hash == hash } }
				)
			}
		} else
			DataResult.Error(ValueIsNullError())
	}

	override suspend fun setPageLimit(limit: Int) { pagination.setPageLimit(limit = limit) }

	override suspend fun loadLocal() {
		pagination.loadLocal()
	}

	override suspend fun load() {
		pagination.load()
	}
}
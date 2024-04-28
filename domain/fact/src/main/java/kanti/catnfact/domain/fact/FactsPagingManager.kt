package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FactsPagingManager @Inject constructor(
	settingsRepository: SettingsRepository,
	private val factRepository: FactRepository,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) : Pagination<Fact> {

	private val pagination = object : BasePagination<String>(25) {

		override suspend fun loadLocal0(limit: Int): List<String> {
			return factRepository.getLocalFactsHashes(limit = limit)
		}

		override suspend fun load0(page: Int, limit: Int): DataResult<List<String>, DataError> {
			return factRepository.loadFacts(page = page, limit = limit)
		}
	}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override val data: Flow<DataResult<List<Fact>, DataError>> = pagination.data
		.combine(settingsRepository.settings) { paginatedData, settings ->
			val error = paginatedData.error
			val paginatedHashes = paginatedData.value

			if (error != null) {
				DataResult.Error(
					error = error,
					value = paginatedHashes?.let { hashes ->
						val translatedFacts = getTranslatedFactsUseCase(
							hashes = hashes,
							translateEnabled = settings.autoTranslate
						).value ?: return@let null
						hashes.map { hash -> translatedFacts.first { it.hash == hash } }
					}
				)
			} else if (paginatedHashes != null) {
				val translatedFacts = getTranslatedFactsUseCase(
					hashes = paginatedHashes,
					translateEnabled = settings.autoTranslate
				)
				translatedFacts.runIfNotError { facts ->
					DataResult.Success(
						value = paginatedHashes.map { hash -> facts.first { it.hash == hash } }
					)
				}
			} else
				DataResult.Error(ValueIsNullError())
		}

	override fun updateData() {
		pagination.updateData()
	}

	override suspend fun loadLocal() {
		pagination.loadLocal()
	}

	override suspend fun load() {
		pagination.load()
	}
}
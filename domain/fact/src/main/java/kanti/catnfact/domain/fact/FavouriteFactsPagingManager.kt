package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.paging.PagingResult
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FavouriteFactsPagingManager @Inject constructor(
	settingsRepository: SettingsRepository,
	private val factRepository: FactRepository,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) : Pagination<Fact> {

	private val pagination = object : BasePagination<String>(25) {
		override suspend fun loadLocal0(limit: Int): List<String> = listOf()

		override suspend fun load0(page: Int, limit: Int): DataResult<List<String>, DataError> {
			val facts = factRepository.localFavouritesFacts(page = page, limit = limit)
			return DataResult.Success(facts)
		}
	}

	override val data: Flow<PagingResult<Fact>> = pagination.data
		.combine(settingsRepository.settings) { hashesResult, settings ->
			val translatedFacts = hashesResult.data.runIfNotError { hashes ->
				getTranslatedFactsUseCase(
					hashes = hashes,
					translateEnabled = settings.autoTranslate
				)
			}
			PagingResult(
				data = translatedFacts,
				isFinally = hashesResult.isFinally
			)
		}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override fun updateData() = pagination.updateData()

	override suspend fun loadLocal() = pagination.loadLocal()

	override suspend fun load() = pagination.load()
}
package kanti.catnfact.domain.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.paging.PagingResult
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.breed.translated.GetTranslatedBreedsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class BreedsPagingManager @Inject constructor(
	private val breedRepository: BreedRepository,
	settingsRepository: SettingsRepository,
	private val getTranslatedBreedsUseCase: GetTranslatedBreedsUseCase
) : Pagination<Breed> {

	private val pagination = object : BasePagination<String>(25) {

		override suspend fun loadLocal0(limit: Int): List<String> {
			return breedRepository.getLocalBreedsHashes(limit = limit)
		}

		override suspend fun load0(page: Int, limit: Int): DataResult<List<String>, DataError> {
			return breedRepository.loadFacts(page = page, limit = limit)
		}
	}

	override val data: Flow<PagingResult<Breed>> = pagination.data
		.combine(settingsRepository.settings) { dataResult, settings ->
			val error = dataResult.data.error
			val paginatedHashes = dataResult.data.value

			if (error != null) {
				return@combine PagingResult(
					data = DataResult.Error(
						error = error,
						value = paginatedHashes?.getOrderedBreeds(settings.autoTranslate)?.value
					),
					isFinally = true
				)
			}
			if (paginatedHashes == null) {
				return@combine PagingResult(
					data = DataResult.Error(ValueIsNullError()),
					isFinally = true
				)
			}

			val result: DataResult<List<Breed>, DataError> = paginatedHashes.getOrderedBreeds(settings.autoTranslate)
			PagingResult(
				data = result,
				isFinally = dataResult.isFinally
			)
		}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override fun updateData() = pagination.updateData()

	override suspend fun loadLocal() = pagination.loadLocal()

	override suspend fun load() = pagination.load()

	private suspend fun List<String>.getOrderedBreeds(
		translateEnabled: Boolean
	): DataResult<List<Breed>, DataError> {
		val translatedBreeds = getTranslatedBreedsUseCase(hashes = this, translateEnabled = translateEnabled)
		return translatedBreeds.runIfNotError { breeds ->
			val orderedBreeds = mapNotNull { hash ->
				breeds.firstOrNull { breed -> breed.hash == hash }
			}
			DataResult.Success(orderedBreeds)
		}
	}
}
package kanti.catnfact.domain.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
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

class FavouriteBreedsPagingManager @Inject constructor(
	settingsRepository: SettingsRepository,
	private val breedRepository: BreedRepository,
	private val getTranslatedBreedsUseCase: GetTranslatedBreedsUseCase
) : Pagination<Breed> {

	private val pagination = object : BasePagination<String>(25) {

		override suspend fun loadLocal0(limit: Int): List<String> = listOf()

		override suspend fun load0(page: Int, limit: Int): DataResult<List<String>, DataError> {
			val breeds = breedRepository.getLocalFavouriteBreedHashes(page = page, limit = limit)
			return DataResult.Success(breeds)
		}
	}

	override val data: Flow<PagingResult<Breed>> = pagination.data
		.combine(settingsRepository.settings) { data, settingsData ->
			val translatedBreeds = data.data.runIfNotError { hashes ->
				getTranslatedBreedsUseCase(
					hashes = hashes,
					translateEnabled = settingsData.autoTranslate
				)
			}

			PagingResult(
				data = translatedBreeds,
				isFinally = data.isFinally
			)
		}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override fun updateData() = pagination.updateData()

	override suspend fun loadLocal() = pagination.loadLocal()

	override suspend fun load() = pagination.load()
}
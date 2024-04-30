package kanti.catnfact.domain.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.paging.PagingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BreedsPagingManager @Inject constructor(
	private val breedRepository: BreedRepository
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
		.map { dataResult ->
			val error = dataResult.data.error
			val paginatedHashes = dataResult.data.value

			if (error != null) {
				return@map PagingResult(
					data = DataResult.Error(
						error = error,
						value = paginatedHashes?.getOrderedBreeds()
					),
					isFinally = true
				)
			}
			if (paginatedHashes == null) {
				return@map PagingResult(
					data = DataResult.Error(ValueIsNullError()),
					isFinally = true
				)
			}

			val result: DataResult<List<Breed>, DataError> = DataResult.Success(value = paginatedHashes.getOrderedBreeds())
			PagingResult(
				data = result,
				isFinally = dataResult.isFinally
			)
		}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override fun updateData() = pagination.updateData()

	override suspend fun loadLocal() = pagination.loadLocal()

	override suspend fun load() = pagination.load()

	private suspend fun List<String>.getOrderedBreeds(): List<Breed> {
		val breeds = breedRepository.getLocalBreeds(hashes = this)
		return mapNotNull { hash ->
			breeds.firstOrNull { breed -> breed.hash == hash }
		}
	}
}
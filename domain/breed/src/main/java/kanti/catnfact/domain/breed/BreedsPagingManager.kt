package kanti.catnfact.domain.breed

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.paging.BasePagination
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.data.runIfNotError
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

	override val data: Flow<DataResult<List<Breed>, DataError>> = pagination.data
		.map { dataResult ->
			dataResult.runIfNotError { hashes ->
				val breeds = breedRepository.getLocalBreeds(hashes = hashes)
				DataResult.Success(
					value = hashes.map { hash ->
						breeds.first { breed -> breed.hash == hash }
					}
				)
			}
		}

	override val isNoMore: Flow<Boolean> = pagination.isNoMore

	override fun updateData() = pagination.updateData()

	override suspend fun loadLocal() = pagination.loadLocal()

	override suspend fun load() = pagination.load()
}
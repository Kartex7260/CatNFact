package kanti.catnfact.data.model.breed.translated

import kanti.catnfact.data.model.breed.translated.datasource.local.TranslatedBreedLocalDataSource
import kanti.catnfact.data.model.breed.translated.datasource.remote.TranslatedBreedRemoteDataSource
import javax.inject.Inject

class TranslatedBreedRepositoryImpl @Inject constructor(
	private val localDataSource: TranslatedBreedLocalDataSource,
	private val remoteDataSource: TranslatedBreedRemoteDataSource
) : TranslatedBreedRepository {
}
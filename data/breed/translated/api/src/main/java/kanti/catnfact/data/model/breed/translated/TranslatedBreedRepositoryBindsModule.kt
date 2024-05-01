package kanti.catnfact.data.model.breed.translated

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedBreedRepositoryBindsModule {

	@Binds
	@Singleton
	fun bindTranslatedBreedRepositoryImpl(
		repository: TranslatedBreedRepositoryImpl
	): TranslatedBreedRepository
}
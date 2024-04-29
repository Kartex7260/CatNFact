package kanti.catnfact.data.model.breed

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BreedRepositoryBindsModule {

	@Binds
	@Singleton
	fun bindBreedRepositoryImpl(repository: BreedRepositoryImpl): BreedRepository
}
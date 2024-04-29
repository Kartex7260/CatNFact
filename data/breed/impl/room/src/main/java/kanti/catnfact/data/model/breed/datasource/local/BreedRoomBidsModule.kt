package kanti.catnfact.data.model.breed.datasource.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BreedRoomBidsModule {

	@Binds
	@Singleton
	fun bindBreedRoomDataSource(
		dataSource: BreedRoomDataSource
	): BreedLocalDataSource
}
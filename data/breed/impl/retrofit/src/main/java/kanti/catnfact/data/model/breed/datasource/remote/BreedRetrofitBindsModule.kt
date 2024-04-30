package kanti.catnfact.data.model.breed.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BreedRetrofitBindsModule {

	@Binds
	@Singleton
	fun bindBreedRetrofitDataSource(
		dataSource: BreedRetrofitDataSource
	): BreedRemoteDataSource
}
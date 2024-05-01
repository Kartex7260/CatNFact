package kanti.catnfact.data.model.breed.translated.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedBreedRetrofitBindsModule {

	@Binds
	@Singleton
	fun bindTranslatedBreedRetrofitDataSource(
		dataSource: TranslatedBreedRetrofitDataSource
	): TranslatedBreedRemoteDataSource
}
package kanti.catnfact.data.model.breed.translator.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.model.breed.translated.datasource.remote.TranslatedBreedRemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedBreedLibreTranslateBindsModule {

	@Binds
	@Singleton
	fun bindTranslatedBreedRetrofitDataSource(
		dataSource: TranslatedBreedRetrofitDataSource
	): TranslatedBreedRemoteDataSource
}
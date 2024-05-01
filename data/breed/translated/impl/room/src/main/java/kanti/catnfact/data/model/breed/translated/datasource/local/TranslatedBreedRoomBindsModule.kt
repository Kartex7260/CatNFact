package kanti.catnfact.data.model.breed.translated.datasource.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedBreedRoomBindsModule {

	@Binds
	@Singleton
	fun bindTranslatedBreedRoomDataSource(
		dataSource: TranslatedBreedRoomDataSource
	): TranslatedBreedLocalDataSource
}
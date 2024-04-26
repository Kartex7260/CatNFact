package kanti.catnfact.data.model.fact.translated.datasource.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedFactRoomBindModule {

	@Binds
	@Singleton
	fun bindTranslatedFactRoomDataSource(
		dataSource: TranslatedFactRoomDataSource
	): TranslatedFactLocalDataSource
}
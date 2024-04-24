package kanti.catnfact.data.model.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreBindsModule {

	@Singleton
	@Binds
	fun bindSettingsRepositoryImpl(repository: SettingsDataStoreDataSource): SettingsLocalDataSource
}
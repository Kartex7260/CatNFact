package kanti.catnfact.data.model.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindsModule {

	@Binds
	@Singleton
	fun bindsSettingsRepositoryImpl(repository: SettingsRepositoryImpl): SettingsRepository
}
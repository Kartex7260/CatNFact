package kanti.catnfact.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.data.model.settings.SettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreSettingsBinds {

	@Singleton
	@Binds
	fun bindSettingsRepositoryImpl(repository: SettingsRepositoryImpl): SettingsRepository
}
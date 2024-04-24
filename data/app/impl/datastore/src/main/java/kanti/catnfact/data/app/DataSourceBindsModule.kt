package kanti.catnfact.data.app

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceBindsModule {

	@Singleton
	@Binds
	fun bindAppDataStoreDataSource(dataSource: AppDataStoreDataSource): AppDataLocalDataSource
}
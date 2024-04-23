package kanti.catnfact.data.model.fact.datasource.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

	@Binds
	@Singleton
	fun bindFactRoomDataSource(dataSource: FactRoomDataSource): FactLocalDataSource
}
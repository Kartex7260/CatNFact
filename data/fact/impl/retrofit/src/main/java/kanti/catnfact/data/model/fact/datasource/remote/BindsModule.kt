package kanti.catnfact.data.model.fact.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {

	@Binds
	@Singleton
	fun bindFactRetrofitDataSource(dataSource: FactRetrofitDataSource): FactRemoteDataSource
}
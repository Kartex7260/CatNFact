package kanti.catnfact.data.model.fact

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
	fun bindFactRepositoryImpl(repository: FactRepositoryImpl): FactRepository
}
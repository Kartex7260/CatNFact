package kanti.catnfact.data.model.fact.translated

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedFactBindsModule {

	@Singleton
	@Binds
	fun bindTranslatedFactRepositoryImpl(
		repository: TranslatedFactRepositoryImpl
	): TranslatedFactRepository
}
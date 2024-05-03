package kanti.catnfact.data.model.fact.translated.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TranslatedFactsLibreTranslateBindsModule {

	@Binds
	@Singleton
	fun bindTranslatedFactsRetrofitDataSource(
		dataSource: TranslatedFactsRetrofitDataSource
	): TranslatedFactRemoteDataSource
}
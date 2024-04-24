package kanti.catnfact.data.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.retrofit.fact.FactService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvidesModule {

	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit = buildRetrofit()

	@Provides
	@Singleton
	fun provideFactService(retrofit: Retrofit): FactService = buildFactService(retrofit)
}
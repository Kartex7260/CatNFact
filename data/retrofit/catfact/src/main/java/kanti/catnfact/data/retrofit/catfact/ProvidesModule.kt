package kanti.catnfact.data.retrofit.catfact

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.retrofit.catfact.breed.BreedService
import kanti.catnfact.data.retrofit.catfact.fact.FactService
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvidesModule {

	@Provides
	@Singleton
	@CatFactApiRetrofitQualifier
	fun provideRetrofit(): Retrofit = buildRetrofit()

	@Provides
	@Singleton
	fun provideFactService(
		@CatFactApiRetrofitQualifier retrofit: Retrofit
	): FactService = buildFactService(retrofit)

	@Provides
	@Singleton
	fun provideBreedService(
		@CatFactApiRetrofitQualifier retrofit: Retrofit
	): BreedService = buildBreedService(retrofit)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatFactApiRetrofitQualifier
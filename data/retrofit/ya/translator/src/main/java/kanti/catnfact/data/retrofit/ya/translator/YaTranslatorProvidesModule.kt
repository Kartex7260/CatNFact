package kanti.catnfact.data.retrofit.ya.translator

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YaTranslatorProvidesModule {

	@Provides
	@Singleton
	@YaTranslatorRetrofitQualifier
	fun bindRetrofit(): Retrofit = buildRetrofit()

	@Provides
	@Singleton
	fun bindYaTranslatorService(
		@YaTranslatorRetrofitQualifier retrofit: Retrofit
	) = buildYaTranslatorService(retrofit)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YaTranslatorRetrofitQualifier
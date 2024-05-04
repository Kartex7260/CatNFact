package kanti.catnfact.data.retrofit.libretranslate

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibreTranslateProvidesModule {

	@Provides
	@Singleton
	@LibreTranslateRetrofitQualifier
	fun bindLibreTranslateRetrofit(): Retrofit = buildRetrofit()

	@Provides
	@Singleton
	fun bindLibreTranslateService(
		@LibreTranslateRetrofitQualifier retrofit: Retrofit
	): LibreTranslateService = buildLibreTranslateService(retrofit)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LibreTranslateRetrofitQualifier
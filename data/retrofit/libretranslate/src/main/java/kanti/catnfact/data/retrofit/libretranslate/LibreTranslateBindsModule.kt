package kanti.catnfact.data.retrofit.libretranslate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LibreTranslateBindsModule {

	@Binds
	@Singleton
	@LibreTranslateRetrofitQualifier
	fun bindLibreTranslateRetrofit(): Retrofit = buildRetrofit()

	@Binds
	@Singleton
	fun bindLibreTranslateService(
		@LibreTranslateRetrofitQualifier retrofit: Retrofit
	): LibreTranslateService = buildLibreTranslateService(retrofit)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LibreTranslateRetrofitQualifier
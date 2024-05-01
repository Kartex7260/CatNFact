package kanti.catnfact.data.room

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.room.breed.BreedDao
import kanti.catnfact.data.room.breed.translated.TranslatedBreedDao
import kanti.catnfact.data.room.fact.FactDao
import kanti.catnfact.data.room.fact.translated.TranslatedFactDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideModule {

	@Provides
	@Singleton
	fun provideCatNFactDatabase(
		@ApplicationContext context: Context
	): CatNFactDatabase = roomBuilder(context)

	@Provides
	@Singleton
	fun provideFactDao(database: CatNFactDatabase): FactDao = database.factDao()

	@Provides
	@Singleton
	fun provideTranslatedFactDao(database: CatNFactDatabase): TranslatedFactDao = database.translatedFactDao()

	@Provides
	@Singleton
	fun provideBreedDao(database: CatNFactDatabase): BreedDao = database.breedDao()

	@Provides
	@Singleton
	fun provideTranslatedBreedDao(database: CatNFactDatabase): TranslatedBreedDao = database.translatedBreedDao()
}
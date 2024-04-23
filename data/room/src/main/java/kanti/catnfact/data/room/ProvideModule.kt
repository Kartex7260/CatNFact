package kanti.catnfact.data.room

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.room.fact.FactDao
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
}
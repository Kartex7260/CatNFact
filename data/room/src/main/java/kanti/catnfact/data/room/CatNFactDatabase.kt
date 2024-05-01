package kanti.catnfact.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.catnfact.data.room.breed.BreedDao
import kanti.catnfact.data.room.breed.BreedEntity
import kanti.catnfact.data.room.breed.translated.TranslatedBreedDao
import kanti.catnfact.data.room.breed.translated.TranslatedBreedEntity
import kanti.catnfact.data.room.fact.FactDao
import kanti.catnfact.data.room.fact.FactEntity
import kanti.catnfact.data.room.fact.translated.TranslatedFactDao
import kanti.catnfact.data.room.fact.translated.TranslatedFactEntity

@Database(
	entities = [
		FactEntity::class, TranslatedFactEntity::class,
		BreedEntity::class, TranslatedBreedEntity::class
	],
	version = 1,
	exportSchema = true
)
abstract class CatNFactDatabase : RoomDatabase() {

	abstract fun factDao(): FactDao

	abstract fun translatedFactDao(): TranslatedFactDao

	abstract fun breedDao(): BreedDao

	abstract fun translatedBreedDao(): TranslatedBreedDao
}
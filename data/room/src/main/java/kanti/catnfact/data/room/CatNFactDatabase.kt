package kanti.catnfact.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.catnfact.data.room.fact.FactDao
import kanti.catnfact.data.room.fact.FactEntity

@Database(
	entities = [ FactEntity::class ],
	version = 1,
	exportSchema = true
)
abstract class CatNFactDatabase : RoomDatabase() {

	abstract fun factDao(): FactDao
}
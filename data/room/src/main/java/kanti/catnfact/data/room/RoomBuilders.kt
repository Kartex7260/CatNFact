package kanti.catnfact.data.room

import android.content.Context
import androidx.room.Room

private const val databaseName = "catNFact"

fun roomBuilder(context: Context): CatNFactDatabase {
	return Room.databaseBuilder(
		context = context,
		klass = CatNFactDatabase::class.java,
		name = databaseName
	).build()
}
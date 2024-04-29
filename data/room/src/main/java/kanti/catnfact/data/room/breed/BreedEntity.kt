package kanti.catnfact.data.room.breed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
	@PrimaryKey val hash: String = "",

	val breed: String = "",
	val country: String = "",
	val origin: String = "",
	val coat: String = "",
	val pattern: String = "",

	@ColumnInfo("is_favourite") val isFavourite: Boolean = false
)

package kanti.catnfact.data.room.fact

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts")
data class FactEntity(
	@PrimaryKey val hash: String = "",
	val fact: String = "",
	@ColumnInfo(name = "is_favourite") val isFavourite: Boolean = false
)

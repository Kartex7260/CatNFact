package kanti.catnfact.data.room.breed.translated

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_breeds")
data class TranslatedBreedEntity(
	@PrimaryKey val hash: String = "",

	val breed: String = "",
	val country: String = "",
	val origin: String = "",
	val coat: String = "",
	val pattern: String = "",

	@ColumnInfo(name = "from_locale_code") val fromLocaleCode: String = "",
	@ColumnInfo(name = "target_locale_code") val targetLocaleCode: String = ""
)

package kanti.catnfact.data.room.fact.translated

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_facts")
data class TranslatedFactEntity(
	@PrimaryKey val hash: String = "",
	val fact: String = "",
	@ColumnInfo(name = "from_locale_code") val fromLocaleCode: String = "",
	@ColumnInfo(name = "destination_locale_code") val destinationLocaleCode: String = ""
)

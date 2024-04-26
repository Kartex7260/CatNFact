package kanti.catnfact.data.room.fact.translated

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TranslatedFactDao {

	@Query("SELECT * FROM translated_facts WHERE hash IN (:hashes) AND from_locale_code = :fromLocaleCode " +
			"AND destination_locale_code = :destinationLocaleCode LIMIT :limit")
	suspend fun getTranslatedFacts(
		hashes: List<String>,
		fromLocaleCode: String,
		destinationLocaleCode: String,
		limit: Int = hashes.size
	): List<TranslatedFactEntity>

	@Query("SELECT hash FROM translated_facts WHERE hash IN (:hashes) AND from_locale_code = :fromLocaleCode " +
			"AND destination_locale_code = :destinationLocaleCode LIMIT :limit")
	suspend fun getHashes(
		hashes: List<String>,
		fromLocaleCode: String,
		destinationLocaleCode: String,
		limit: Int = hashes.size
	): List<String>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(facts: List<TranslatedFactEntity>)
}
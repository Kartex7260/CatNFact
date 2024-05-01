package kanti.catnfact.data.room.breed.translated

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TranslatedBreedDao {

	@Query("SELECT hash FROM translated_breeds WHERE hash IN (:hashes) " +
			"AND from_locale_code = :fromLocaleCode " +
			"AND target_locale_code = :targetLocaleCode " +
			"LIMIT :limit")
	suspend fun getHashes(
		hashes: List<String>,
		fromLocaleCode: String,
		targetLocaleCode: String,
		limit: Int = hashes.size
	): List<String>

	@Query("SELECT * FROM translated_breeds WHERE hash IN (:hashes) " +
			"AND from_locale_code = :fromLocaleCode " +
			"AND target_locale_code = :targetLocaleCode " +
			"LIMIT :limit")
	suspend fun getBreeds(
		hashes: List<String>,
		fromLocaleCode: String,
		targetLocaleCode: String,
		limit: Int = hashes.size
	): List<TranslatedBreedEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(breeds: List<TranslatedBreedEntity>)
}
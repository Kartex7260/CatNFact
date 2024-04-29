package kanti.catnfact.data.room.breed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedDao {

	@Query("UPDATE breeds SET is_favourite = NOT(is_favourite) WHERE hash = :hash")
	suspend fun changeFavourite(hash: String)

	@Query("SELECT hash FROM breeds LIMIT :limit")
	suspend fun getBreedsHashes(limit: Int): List<String>

	@Query("SELECT * FROM breeds WHERE hash IN (:hashes) LIMIT :limit")
	suspend fun getBreeds(hashes: List<String>, limit: Int = hashes.size): List<BreedEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(breeds: List<BreedEntity>)
}
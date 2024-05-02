package kanti.catnfact.data.room.fact

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FactDao {

	@Query("SELECT hash FROM facts WHERE is_favourite = 1 LIMIT :limit OFFSET :offset")
	suspend fun getHashes(limit: Int, offset: Int): List<String>

	@Query("SELECT hash FROM facts LIMIT :limit")
	suspend fun getAllHashes(limit: Int): List<String>

	@Query("SELECT * FROM facts WHERE hash IN (:hashes) LIMIT :limit")
	suspend fun getFactsList(hashes: List<String>, limit: Int = hashes.size): List<FactEntity>

	@Query("SELECT * FROM facts WHERE hash = :hash LIMIT 1")
	suspend fun getFact(hash: String): FactEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(fact: FactEntity)

	@Query("UPDATE facts SET is_favourite = NOT(is_favourite) WHERE hash = :hash")
	suspend fun changeFavourite(hash: String)
}
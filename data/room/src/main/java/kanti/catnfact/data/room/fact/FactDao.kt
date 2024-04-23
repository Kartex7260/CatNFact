package kanti.catnfact.data.room.fact

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FactDao {

	@Query("SELECT hash FROM facts")
	suspend fun getAllHashes(): List<String>

	@Query("SELECT * FROM facts WHERE hash = :hash LIMIT 1")
	suspend fun getFact(hash: String): FactEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(fact: FactEntity)

	@Query("UPDATE facts SET is_favourite = NOT(is_favourite) WHERE hash = :hash")
	suspend fun changeFavourite(hash: String)
}
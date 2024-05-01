package kanti.catnfact.data.model.breed.translated.datasource.local

import kanti.catnfact.data.model.breed.translated.BreedData
import kanti.catnfact.data.room.breed.translated.TranslatedBreedDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslatedBreedRoomDataSource @Inject constructor(
	private val translatedBreedDao: TranslatedBreedDao
) : TranslatedBreedLocalDataSource {

	override suspend fun getUntranslated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): List<BreedData> {
		return withContext(Dispatchers.Default) {
			val translated = translatedBreedDao.getHashes(
				hashes = breeds.map { it.hash },
				fromLocaleCode = fromLocaleCode,
				targetLocaleCode = targetLocaleCode
			)
			return@withContext breeds.filter { breed ->
				!translated.contains(breed.hash)
			}
		}
	}

	override suspend fun getBreeds(hashes: List<String>, fromLocaleCode: String, targetLocaleCode: String): List<BreedData> {
		return withContext(Dispatchers.Default) {
			translatedBreedDao.getBreeds(
				hashes = hashes,
				fromLocaleCode = fromLocaleCode,
				targetLocaleCode = targetLocaleCode
			).map { it.toBreed() }
		}
	}

	override suspend fun insert(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	) {
		withContext(Dispatchers.Default) {
			translatedBreedDao.insert(
				breeds = breeds.map {
					it.toEntity(
						fromLocaleCode = fromLocaleCode,
						targetLocaleCode = targetLocaleCode
					)
				}
			)
		}
	}
}
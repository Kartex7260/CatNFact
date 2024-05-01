package kanti.catnfact.data.model.breed.translated

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.breed.translated.datasource.local.TranslatedBreedLocalDataSource
import kanti.catnfact.data.model.breed.translated.datasource.remote.TranslatedBreedRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslatedBreedRepositoryImpl @Inject constructor(
	private val localDataSource: TranslatedBreedLocalDataSource,
	private val remoteDataSource: TranslatedBreedRemoteDataSource
) : TranslatedBreedRepository {

	override suspend fun translate(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): DataResult<List<BreedData>, DataError> = withContext(Dispatchers.Default) {
		val untranslated = localDataSource.getUntranslated(breeds, fromLocaleCode, targetLocaleCode)
		if (untranslated.isNotEmpty()) {
			val remoteTranslated = remoteDataSource.translated(
				breeds = breeds,
				fromLocaleCode = fromLocaleCode,
				targetLocaleCode = targetLocaleCode
			)

			val remoteError = remoteTranslated.error
			if (remoteError != null) {
				return@withContext DataResult.Error(
					error = remoteError,
					value = getTranslated(
						breeds = breeds,
						fromLocaleCode = fromLocaleCode,
						targetLocaleCode = targetLocaleCode
					)
				)
			}

			val value = remoteTranslated.value ?: return@withContext DataResult
				.Error(ValueIsNullError("${this::class.qualifiedName}: remoteTranslated"))
			localDataSource.insert(
				breeds = value,
				fromLocaleCode = fromLocaleCode,
				targetLocaleCode = targetLocaleCode
			)
		}

		val translatedBreeds = getTranslated(
			breeds = breeds,
			fromLocaleCode = fromLocaleCode,
			targetLocaleCode = targetLocaleCode
		)
		return@withContext DataResult.Success(translatedBreeds)
	}

	private suspend fun getTranslated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): List<BreedData> {
		val localBreeds = localDataSource.getBreeds(
			hashes = breeds.map { it.hash },
			fromLocaleCode = fromLocaleCode,
			targetLocaleCode = targetLocaleCode
		)
		return breeds.map { breed ->
			val translated = localBreeds.firstOrNull { it.hash == breed.hash }
			breed.copy(
				breed = translated?.breed ?: breed.breed,
				country = translated?.country ?: breed.country,
				origin = translated?.origin ?: breed.origin,
				coat = translated?.coat ?: breed.coat,
				pattern = translated?.pattern ?: breed.pattern
			)
		}
	}
}
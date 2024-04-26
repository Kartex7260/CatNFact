package kanti.catnfact.data.model.fact.translated

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact
import kanti.catnfact.data.model.fact.translated.datasource.local.TranslatedFactLocalDataSource
import kanti.catnfact.data.model.fact.translated.datasource.remote.TranslatedFactRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslatedFactRepositoryImpl @Inject constructor(
	private val localDataSource: TranslatedFactLocalDataSource,
	private val remoteDataSource: TranslatedFactRemoteDataSource
) : TranslatedFactRepository {

	override suspend fun translate(
		facts: Sequence<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): DataResult<List<TranslatedFact>, DataError> {
		return withContext(Dispatchers.Default) {
			val untranslated = localDataSource.returnUntranslated(
				facts = facts,
				fromLocaleCode = fromLocaleCode,
				destinationLocaleCode = destinationLocaleCode
			)

			if (untranslated.isNotEmpty()) {
				val translated = remoteDataSource.translate(
					facts = untranslated,
					fromLocaleCode = fromLocaleCode,
					destinationLocaleCode = destinationLocaleCode
				)

				val error = translated.error
				if (error != null) {
					val localFacts = localDataSource.getTranslatedFacts(
						facts = facts.map { it.hash },
						fromLocaleCode = fromLocaleCode,
						destinationLocaleCode = destinationLocaleCode
					)

					return@withContext DataResult.Error(
						error = error,
						value = facts.map { fact ->
							localFacts.firstOrNull { fact.hash == it.hash } ?: TranslatedFact(
								hash = fact.hash,
								fact = fact.fact,
								fromLocaleCode = fromLocaleCode,
								destinationLocaleCode = fromLocaleCode
							)
						}.toList()
					)
				}

				val translatedFacts = translated.value ?: return@withContext DataResult.Error(ValueIsNullError())
				localDataSource.insert(translatedFacts)
			}


			val translatedFacts = localDataSource.getTranslatedFacts(
				facts = facts.map { it.hash },
				fromLocaleCode = fromLocaleCode,
				destinationLocaleCode = destinationLocaleCode
			)
			return@withContext DataResult.Success(translatedFacts)
		}
	}
}
package kanti.catnfact.data.model.fact.translated.datasource.local

import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact
import kanti.catnfact.data.room.fact.translated.TranslatedFactDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslatedFactRoomDataSource @Inject constructor(
	private val translatedFactDao: TranslatedFactDao
) : TranslatedFactLocalDataSource {

	override suspend fun getTranslatedFacts(
		facts: Sequence<String>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): List<TranslatedFact> {
		return withContext(Dispatchers.Default) {
			translatedFactDao.getTranslatedFacts(
				hashes = facts.toList(),
				fromLocaleCode = fromLocaleCode,
				destinationLocaleCode = destinationLocaleCode
			).map { it.toTranslatedFact() }
		}
	}

	override suspend fun returnUntranslated(
		facts: Sequence<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): List<HalfFact> {
		return withContext(Dispatchers.Default) {
			val hashes = translatedFactDao.getHashes(
				hashes = facts.map { it.hash }.toList(),
				fromLocaleCode = fromLocaleCode,
				destinationLocaleCode = destinationLocaleCode
			)
			facts.filter { fact -> !hashes.contains(fact.hash) }.toList()
		}
	}

	override suspend fun insert(facts: List<TranslatedFact>) {
		withContext(Dispatchers.Default) {
			translatedFactDao.insert(
				facts = facts.map { it.toEntity() }
			)
		}
	}
}
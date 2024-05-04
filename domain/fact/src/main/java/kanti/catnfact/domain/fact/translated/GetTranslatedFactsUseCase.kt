package kanti.catnfact.domain.fact.translated

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.alsoIfNotError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.model.fact.translated.TranslatedFactRepository
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact
import kanti.catnfact.data.runIfNotError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetTranslatedFactsUseCase @Inject constructor(
	@ApplicationContext private val context: Context,
	private val translatedFactRepository: TranslatedFactRepository,
	private val factRepository: FactRepository
) {

	suspend operator fun invoke(
		hashes: List<String>,
		translateEnabled: Boolean = true
	): DataResult<List<Fact>, DataError> {
		return withContext(Dispatchers.Default) {
			var facts = factRepository.getLocalFacts(hashes)

			if (translateEnabled) {
				val currentLocaleCode = context.resources.configuration.locales.get(0).language
				val translatedFactsResult = translatedFactRepository.translate(
					facts = facts.asSequence().map { fact ->
						HalfFact(hash = fact.hash, fact = fact.fact)
					},
					fromLocaleCode = SOURCE_LOCALE,
					destinationLocaleCode = currentLocaleCode
				)
				translatedFactsResult.alsoIfNotError { translated ->
					facts = facts.map { fact ->
						fact.copy(
							fact = translated.first { it.hash == fact.hash }.fact
						)
					}
				}
			}

			DataResult.Success(facts)
		}
	}

	companion object {

		private val SOURCE_LOCALE = Locale.US.language
	}
}
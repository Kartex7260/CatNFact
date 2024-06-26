package kanti.catnfact.data.model.fact.translated.datasource.remote

import kanti.catnfact.data.BadRequestError
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact
import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import kanti.catnfact.data.retrofit.libretranslate.translate.TranslateBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TranslatedFactsRetrofitDataSource @Inject constructor(
	private val libreTranslateService: LibreTranslateService
) : TranslatedFactRemoteDataSource {

	override suspend fun translate(
		facts: List<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): DataResult<List<TranslatedFact>, DataError> = withContext(Dispatchers.Default) {
		try {
			val text = facts.joinToString(SEPARATOR)
			val body = TranslateBody(
				text = text,
				sourceLocaleCode = fromLocaleCode,
				targetLocaleCode = destinationLocaleCode,
			)
			val translatedResponse = libreTranslateService.translate(body)
			val translatedLines = translatedResponse.text.split(SEPARATOR)
			val translatedFacts = facts.mapIndexed { index, halfFact ->
				val translatedLine = translatedLines.getOrNull(index)
				TranslatedFact(
					hash = halfFact.hash,
					fact = translatedLine ?: halfFact.fact,
					fromLocaleCode = fromLocaleCode,
					destinationLocaleCode = destinationLocaleCode
				)
			}
			DataResult.Success(translatedFacts)
		} catch (ex: IOException) {
			DataResult.Error<List<TranslatedFact>, DataError>(
				error = NoConnectionError(ex.message, ex)
			)
		} catch (ex: retrofit2.HttpException) {
			when (ex.code()) {
				400 -> DataResult.Error(error = BadRequestError(ex.message, ex))
				else -> DataResult.Error(error = UnexpectedError(ex.message, ex))
			}
		}
	}

	companion object {

		private const val SEPARATOR = "\n"
	}
}
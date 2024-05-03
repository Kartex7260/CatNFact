package kanti.catnfact.data.model.fact.translated.datasource.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.fact.translated.TranslatedFact
import kanti.catnfact.data.model.fact.translated.datasource.HalfFact
import kanti.catnfact.data.retrofit.ya.translator.TranslateBody
import kanti.catnfact.data.retrofit.ya.translator.YaTranslatorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TranslatedFactRetrofitDataSource @Inject constructor(
	private val yaTranslatorService: YaTranslatorService
) : TranslatedFactRemoteDataSource {

	@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
	override suspend fun translate(
		facts: List<HalfFact>,
		fromLocaleCode: String,
		destinationLocaleCode: String
	): DataResult<List<TranslatedFact>, DataError> {
		return withContext(Dispatchers.Default) {
			val translateBody = TranslateBody(
				sourceLanguageCode = fromLocaleCode,
				targetLanguageCode = destinationLocaleCode,
				texts = facts.map { it.fact }
			)
			try {
				val translatedDto = yaTranslatorService.translate(translateBody)
				DataResult.Success(
					value = facts.mapIndexed { index, halfFact ->
						val translatedText = translatedDto.translations[index].text
						TranslatedFact(
							hash = halfFact.hash,
							fact = translatedText,
							fromLocaleCode = fromLocaleCode,
							destinationLocaleCode = destinationLocaleCode
						)
					}
				)
			} catch (ex: IOException) {
				DataResult.Error(NoConnectionError(ex.message, ex))
			}  catch (ex: HttpException) {
				DataResult.Error(UnexpectedError(ex.message, ex))
			}
		}
	}
}
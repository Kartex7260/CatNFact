package kanti.catnfact.data.model.breed.translated.datasource.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.breed.translated.BreedData
import kanti.catnfact.data.retrofit.ya.translator.TranslateBody
import kanti.catnfact.data.retrofit.ya.translator.YaTranslatorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TranslatedBreedRetrofitDataSource @Inject constructor(
	private val yaTranslatorService: YaTranslatorService
) : TranslatedBreedRemoteDataSource {

	@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
	override suspend fun translated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): DataResult<List<BreedData>, DataError> = withContext(Dispatchers.Default) {
		try {
			val translateBody = TranslateBody(
				sourceLanguageCode = fromLocaleCode,
				targetLanguageCode = targetLocaleCode,
				texts = breeds.flatMap { breed ->
					sequenceOf(
						breed.breed,
						breed.country,
						breed.origin,
						breed.coat,
						breed.pattern
					)
				}
			)
			val translated = yaTranslatorService.translate(translateBody = translateBody)

			val translatedBreeds = mutableListOf<BreedData>()
			var index = 0
			while (index < translated.translations.size - 1) {
				val breedData = breeds[index / 5]
				val translatedBreed = breedData.copy(
					breed = translated.translations[index++].text,
					country = translated.translations[index++].text,
					origin = translated.translations[index++].text,
					coat = translated.translations[index++].text,
					pattern = translated.translations[index++].text
				)
				translatedBreeds.add(translatedBreed)
			}

			DataResult.Success(translatedBreeds)
		} catch (ex: IOException) {
			DataResult.Error(NoConnectionError(ex.message, ex))
		} catch (ex: HttpException) {
			DataResult.Error(UnexpectedError(ex.message, ex))
		}
	}
}
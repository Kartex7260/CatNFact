package kanti.catnfact.data.model.breed.translator.datasource.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.breed.translated.BreedData
import kanti.catnfact.data.model.breed.translated.datasource.remote.TranslatedBreedRemoteDataSource
import kanti.catnfact.data.retrofit.libretranslate.translate.LibreTranslateService
import kanti.catnfact.data.retrofit.libretranslate.translate.TranslateBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TranslatedBreedRetrofitDataSource @Inject constructor(
	private val libreTranslateService: LibreTranslateService
) : TranslatedBreedRemoteDataSource {

	@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
	override suspend fun translated(
		breeds: List<BreedData>,
		fromLocaleCode: String,
		targetLocaleCode: String
	): DataResult<List<BreedData>, DataError> = withContext(Dispatchers.Default) {
		try {
			val text = buildString {
				breeds.forEach { breed ->
					append(breed.breed).append(SEPARATOR)
					append(breed.country).append(SEPARATOR)
					append(breed.origin).append(SEPARATOR)
					append(breed.coat).append(SEPARATOR)
					append(breed.pattern).append(SEPARATOR)
				}
			}
			val body = TranslateBody(
				text = text,
				sourceLocaleCode = fromLocaleCode,
				targetLocaleCode = targetLocaleCode
			)
			val response = libreTranslateService.translate(body)
			val translatedLines = response.text.split(SEPARATOR)
			val translatedBreeds = mutableListOf<BreedData>()

			var index = 0
			while (index < translatedLines.size - 1) {
				val breed = breeds.getOrNull(index / 5) ?: break
				val translatedBreed = breed.copy(
					breed = translatedLines[index++],
					country = translatedLines[index++],
					origin = translatedLines[index++],
					coat = translatedLines[index++],
					pattern = translatedLines[index++],
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

	companion object {

		private const val SEPARATOR = "\n"
	}
}
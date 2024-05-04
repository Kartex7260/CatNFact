package kanti.catnfact.domain.breed.translated

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.alsoIfNotError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.model.breed.translated.TranslatedBreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetTranslatedBreedsUseCase @Inject constructor(
	@ApplicationContext private val context: Context,
	private val breedRepository: BreedRepository,
	private val translatedBreedRepository: TranslatedBreedRepository
) {

	suspend operator fun invoke(
		hashes: List<String>,
		translateEnabled: Boolean
	): DataResult<List<Breed>, DataError> = withContext(Dispatchers.Default) {
		var breeds = breedRepository.getLocalBreeds(hashes = hashes)

		val currentLocale = context.resources.configuration.locales[0].language
		if (translateEnabled && currentLocale != ORIGINAL_LOCALE) {
			val translatedBreedsData = translatedBreedRepository.translate(
				breeds = breeds.map { it.toData()},
				fromLocaleCode = ORIGINAL_LOCALE,
				targetLocaleCode = currentLocale
			)
			translatedBreedsData.alsoIfNotError { breedsData ->
				breeds = breeds.map { breed ->
					val translated = breedsData.firstOrNull { it.hash == breed.hash }
					breed.toTranslated(translated)
				}
			}
		}
		DataResult.Success(breeds)
	}

	companion object {

		private val ORIGINAL_LOCALE = Locale.US.language
	}
}
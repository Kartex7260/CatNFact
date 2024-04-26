package kanti.catnfact.data.model.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
	private val localDataSource: SettingsLocalDataSource
) : SettingsRepository {

	override val settings: Flow<SettingsData> = localDataSource.settingsData
		.map { localSettingsData ->
			SettingsData(
				darkMode = localSettingsData.darkMode ?: return@map run {
					setDarkMode(DARK_MODE_DEFAULT)
					null
				},
				colorStyle = localSettingsData.colorStyle ?: return@map run {
					setColorStyle(COLOR_STYLE_DEFAULT)
					null
				},
				autoTranslate = localSettingsData.autoTranslate ?: return@map run {
					setAutoTranslate(AUTO_TRANSLATE_DEFAULT)
					null
				}
			)
		}
		.filterNotNull()

	override suspend fun setDarkMode(darkMode: DarkMode) {
		localDataSource.setDarkMode(darkMode)
	}

	override suspend fun setColorStyle(colorStyle: ColorStyle) {
		localDataSource.setColorStyle(colorStyle)
	}

	override suspend fun setAutoTranslate(enabled: Boolean) {
		localDataSource.setAutoTranslate(enabled)
	}

	companion object {

		private val DARK_MODE_DEFAULT = DarkMode.AsSystem
		private val COLOR_STYLE_DEFAULT = ColorStyle.CatNFact
		private const val AUTO_TRANSLATE_DEFAULT = true
	}
}
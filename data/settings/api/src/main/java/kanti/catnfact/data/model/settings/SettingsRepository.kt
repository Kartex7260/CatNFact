package kanti.catnfact.data.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

	val settings: Flow<SettingsData>

	suspend fun setDarkMode(darkMode: DarkMode)

	suspend fun setDefaultDarkMode() {
		setDarkMode(DARK_MODE_DEFAULT)
	}

	suspend fun setColorStyle(colorStyle: ColorStyle)

	suspend fun setDefaultColorStyle() {
		setColorStyle(COLOR_STYLE_DEFAULT)
	}

	companion object {

		val DARK_MODE_DEFAULT = DarkMode.AsSystem
		val COLOR_STYLE_DEFAULT = ColorStyle.CatNFact
	}
}
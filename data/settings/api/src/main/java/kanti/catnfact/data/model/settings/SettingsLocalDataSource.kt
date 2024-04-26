package kanti.catnfact.data.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

	val settingsData: Flow<LocalSettingsData>

	suspend fun setDarkMode(darkMode: DarkMode)

	suspend fun setColorStyle(colorStyle: ColorStyle)

	suspend fun setAutoTranslate(enabled: Boolean)
}
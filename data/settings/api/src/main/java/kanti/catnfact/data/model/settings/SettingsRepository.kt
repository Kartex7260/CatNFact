package kanti.catnfact.data.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

	val settings: Flow<SettingsData>

	suspend fun setDarkMode(darkMode: DarkMode)

	suspend fun setColorStyle(colorStyle: ColorStyle)

	suspend fun setAutoTranslate(enabled: Boolean)
}
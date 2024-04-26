package kanti.catnfact.feat.settings

import kanti.catnfact.ui.components.settings.ColorStyleUiState
import kanti.catnfact.ui.components.settings.DarkModeUiState

sealed interface ScreenIntent

data object OnBackIntent : ScreenIntent

sealed interface UiSettingsIntent

data class SetDarkModeIntent(val darkMode: DarkModeUiState) : UiSettingsIntent

data class SetColorStyleIntent(val colorStyle: ColorStyleUiState) : UiSettingsIntent

data class SetAutoTranslate(val enabled: Boolean) : UiSettingsIntent
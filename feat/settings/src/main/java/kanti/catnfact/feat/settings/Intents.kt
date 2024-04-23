package kanti.catnfact.feat.settings

import kanti.catnfact.ui.components.settings.DarkModeUiState

sealed interface ScreenIntent

data object OnBackIntent : ScreenIntent

sealed interface UiSettingsIntent

data class DarkModeIntent(val darkMode: DarkModeUiState) : UiSettingsIntent
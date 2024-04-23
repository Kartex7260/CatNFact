package kanti.catnfact.feat.settings

import androidx.compose.runtime.Immutable
import kanti.catnfact.ui.components.settings.DarkModeUiState

@Immutable
data class UiSettingsUiState(
	val darkMode: DarkModeUiState = DarkModeUiState.AsSystem
)

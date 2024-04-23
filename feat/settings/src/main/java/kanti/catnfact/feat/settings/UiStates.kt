package kanti.catnfact.feat.settings

import androidx.compose.runtime.Immutable
import kanti.catnfact.data.model.settings.DarkMode
import kanti.catnfact.ui.components.settings.DarkModeUiState

@Immutable
data class UiSettingsUiState(
	val darkMode: DarkModeUiState = DarkModeUiState.AsSystem
)

fun DarkMode.toUiState(): DarkModeUiState {
	return when (this) {
		DarkMode.Light -> DarkModeUiState.Light
		DarkMode.Dark -> DarkModeUiState.Dark
		DarkMode.AsSystem -> DarkModeUiState.AsSystem
	}
}

fun DarkModeUiState.toDarkMode(): DarkMode {
	return when (this) {
		DarkModeUiState.Light -> DarkMode.Light
		DarkModeUiState.Dark -> DarkMode.Dark
		DarkModeUiState.AsSystem -> DarkMode.AsSystem
	}
}

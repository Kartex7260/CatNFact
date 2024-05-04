package kanti.catnfact.feat.settings

import androidx.compose.runtime.Immutable
import kanti.catnfact.data.model.settings.ColorStyle
import kanti.catnfact.data.model.settings.DarkMode
import kanti.catnfact.ui.components.settings.ColorStyleUiState
import kanti.catnfact.ui.components.settings.DarkModeUiState

@Immutable
data class UiSettingsUiState(
	val darkMode: DarkModeUiState = DarkModeUiState.AsSystem,
	val colorStyle: ColorStyleUiState = ColorStyleUiState.CatNFact,
	val autoTranslate: AutoTranslateUiState = AutoTranslateUiState()
)

@Immutable
data class AutoTranslateUiState(
	val autoTranslate: Boolean = false,
	val enabled: Boolean = true,
	val visible: Boolean = true
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

fun ColorStyle.toUiState(): ColorStyleUiState {
	return when (this) {
		ColorStyle.CatNFact -> ColorStyleUiState.CatNFact
		ColorStyle.AsSystem -> ColorStyleUiState.AsSystem
	}
}

fun ColorStyleUiState.toColorStyle(): ColorStyle {
	return when (this) {
		ColorStyleUiState.CatNFact -> ColorStyle.CatNFact
		ColorStyleUiState.AsSystem -> ColorStyle.AsSystem
	}
}

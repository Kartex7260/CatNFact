package kanti.catnfact.feat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.ui.components.settings.ColorStyleUiState
import kanti.catnfact.ui.components.settings.DarkModeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSettingsViewModel @Inject constructor(
	private val settingsRepository: SettingsRepository
) : ViewModel() {

	val uiSettings: StateFlow<UiSettingsUiState> = settingsRepository.settings
		.map { settingsData ->
			UiSettingsUiState(
				darkMode = settingsData.darkMode.toUiState(),
				colorStyle = settingsData.colorStyle.toUiState()
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = UiSettingsUiState()
		)

	fun onUiSettingsAction(uiIntent: UiSettingsIntent) {
		when (uiIntent) {
			is SetDarkModeIntent -> setDarkMode(uiIntent.darkMode)
			is SetColorStyleIntent -> setColorStyle(uiIntent.colorStyle)
		}
	}

	private fun setDarkMode(darkMode: DarkModeUiState) {
		viewModelScope.launch {
			settingsRepository.setDarkMode(darkMode.toDarkMode())
		}
	}

	private fun setColorStyle(colorStyle: ColorStyleUiState) {
		viewModelScope.launch {
			settingsRepository.setColorStyle(colorStyle.toColorStyle())
		}
	}
}
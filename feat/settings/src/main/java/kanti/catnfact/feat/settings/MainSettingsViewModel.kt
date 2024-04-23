package kanti.catnfact.feat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.ui.components.settings.DarkModeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainSettingsViewModel @Inject constructor(
	private val settingsRepository: SettingsRepository
) : ViewModel() {

	val uiSettings: StateFlow<UiSettingsUiState> = settingsRepository.settings
		.map { settingsData ->
			UiSettingsUiState(
				darkMode = settingsData.darkMode.toUiState()
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = UiSettingsUiState()
		)

	fun onUiSettingsAction(uiIntent: UiSettingsIntent) {
		when (uiIntent) {
			is DarkModeIntent -> setDarkMode(uiIntent.darkMode)
		}
	}

	private fun setDarkMode(darkMode: DarkModeUiState) {
		viewModelScope.launch {
			settingsRepository.setDarkMode(darkMode.toDarkMode())
		}
	}
}
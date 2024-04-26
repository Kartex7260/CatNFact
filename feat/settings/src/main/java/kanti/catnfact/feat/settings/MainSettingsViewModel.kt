package kanti.catnfact.feat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.model.settings.SettingsRepository
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
				colorStyle = settingsData.colorStyle.toUiState(),
				autoTranslate = settingsData.autoTranslate
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = UiSettingsUiState()
		)

	fun onUiSettingsAction(intent: UiSettingsIntent) {
		when (intent) {
			is SetDarkModeIntent -> setDarkMode(intent)
			is SetColorStyleIntent -> setColorStyle(intent)
			is SetAutoTranslate -> setAutoTranslate(intent)
		}
	}

	private fun setDarkMode(intent: SetDarkModeIntent) {
		viewModelScope.launch {
			settingsRepository.setDarkMode(intent.darkMode.toDarkMode())
		}
	}

	private fun setColorStyle(intent: SetColorStyleIntent) {
		viewModelScope.launch {
			settingsRepository.setColorStyle(intent.colorStyle.toColorStyle())
		}
	}

	private fun setAutoTranslate(intent: SetAutoTranslate) {
		viewModelScope.launch {
			settingsRepository.setAutoTranslate(intent.enabled)
		}
	}
}
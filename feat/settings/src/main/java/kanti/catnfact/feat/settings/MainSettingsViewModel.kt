package kanti.catnfact.feat.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.catnfact.data.model.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainSettingsViewModel @Inject constructor(
	@ApplicationContext context: Context,
	private val settingsRepository: SettingsRepository
) : ViewModel() {

	val uiSettings: StateFlow<UiSettingsUiState> = settingsRepository.settings
		.map { settingsData ->
			UiSettingsUiState(
				darkMode = settingsData.darkMode.toUiState(),
				colorStyle = settingsData.colorStyle.toUiState(),
				autoTranslate = AutoTranslateUiState(
					autoTranslate = settingsData.autoTranslate,
					enabled = false,
					visible = context.resources.configuration.locales[0].language != Locale.US.language
				)
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
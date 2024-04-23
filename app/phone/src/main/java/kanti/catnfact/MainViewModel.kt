package kanti.catnfact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.feat.settings.toUiState
import kanti.catnfact.ui.components.settings.DarkModeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	settingsRepository: SettingsRepository
) : ViewModel() {

	val darkMode: StateFlow<DarkModeUiState> = settingsRepository.settings
		.map { it.darkMode.toUiState() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = DarkModeUiState.AsSystem
		)
}
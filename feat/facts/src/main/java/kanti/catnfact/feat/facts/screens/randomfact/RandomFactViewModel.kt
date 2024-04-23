package kanti.catnfact.feat.facts.screens.randomfact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.NotFoundError
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.feat.facts.toUiState
import kanti.catnfact.ui.components.fact.FactUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RandomFactViewModel @Inject constructor(
	private val factRepository: FactRepository
) : ViewModel() {

	private val mIntent = MutableStateFlow<RandomFactIntent>(NextRandomFactIntent)
	val factUiState: StateFlow<RandomFactUiState> = mIntent
		.map { intent ->
			when (intent) {
				is NextRandomFactIntent -> nextRandomFact()
				is ChangeFavouriteIntent -> changeFavourite(intent)
			}
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = RandomFactUiState()
		)

	fun onAction(intent: RandomFactIntent) {
		mIntent.value = intent
	}

	private suspend fun nextRandomFact(): RandomFactUiState {
		val randomFactResult = factRepository.getRandomFact()

		val fact = randomFactResult.value?.toUiState() ?: FactUiState()
		return RandomFactUiState(
			fact = fact
		)
	}

	private suspend fun changeFavourite(intent: ChangeFavouriteIntent): RandomFactUiState {
		val factResult = factRepository.changeFavourite(intent.hash)

		if (factResult.error is NotFoundError)
			onAction(NextRandomFactIntent)

		val fact = factResult.value?.toUiState() ?: FactUiState()
		return RandomFactUiState(
			fact = fact
		)
	}
}
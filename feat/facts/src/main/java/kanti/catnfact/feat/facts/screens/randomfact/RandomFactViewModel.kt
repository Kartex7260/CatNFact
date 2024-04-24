package kanti.catnfact.feat.facts.screens.randomfact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.NotFoundError
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.domain.fact.GetInitialRandomFactUseCase
import kanti.catnfact.domain.fact.GetRandomFactUseCase
import kanti.catnfact.feat.facts.toUiState
import kanti.catnfact.ui.components.fact.FactUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomFactViewModel @Inject constructor(
	private val factRepository: FactRepository,
	private val getRandomFactUseCase: GetRandomFactUseCase,
	private val getInitialRandomFactUseCase: GetInitialRandomFactUseCase
) : ViewModel() {

	private val mState = MutableStateFlow(RandomFactUiState())
	val factUiState: StateFlow<RandomFactUiState> = mState.asStateFlow()

	init {
		initialRandomFact()
	}

	fun onAction(intent: RandomFactIntent) {
		when (intent) {
			is NextRandomFactIntent -> nextRandomFact()
			is ChangeFavouriteIntent -> changeFavourite(intent)
		}
	}

	private fun initialRandomFact() {
		viewModelScope.launch(Dispatchers.Default) {
			mState.update {
				it.copy(isLoading = true)
			}
			val initialFactsFlow = getInitialRandomFactUseCase()
			initialFactsFlow.collectIndexed { index, factResult ->
				val fact = factResult.value?.toUiState() ?: FactUiState()
				if (index == 0) {
					mState.update {
						it.copy(fact = fact)
					}
				} else {
					mState.value = RandomFactUiState(fact = fact)
				}
			}
		}
	}

	private  fun nextRandomFact() {
		viewModelScope.launch(Dispatchers.Default) {
			mState.update { it.copy(isLoading = true) }
			val randomFactResult = getRandomFactUseCase()

			val fact = randomFactResult.value?.toUiState() ?: FactUiState()
			mState.value = RandomFactUiState(fact = fact)
		}
	}

	private fun changeFavourite(intent: ChangeFavouriteIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			val factResult = factRepository.changeFavourite(intent.hash)

			if (factResult.error is NotFoundError)
				onAction(NextRandomFactIntent)

			val fact = factResult.value?.toUiState() ?: FactUiState()
			mState.value = RandomFactUiState(fact = fact)
		}
	}
}
package kanti.catnfact.feat.facts.screens.randomfact

import androidx.compose.runtime.ProduceStateScope
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
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.yield
import javax.inject.Inject

@HiltViewModel
class RandomFactViewModel @Inject constructor(
	private val factRepository: FactRepository,
	private val getRandomFactUseCase: GetRandomFactUseCase,
	private val getInitialRandomFactUseCase: GetInitialRandomFactUseCase
) : ViewModel() {

	private val mIntent = MutableStateFlow<RandomFactIntent>(InitialRandomFactIntent)
	val factUiState: StateFlow<RandomFactUiState> = mIntent
		.run {
			channelFlow {
				collect { intent ->
					when (intent) {
						is InitialRandomFactIntent -> initialRandomFact()
						is NextRandomFactIntent -> nextRandomFact()
						is ChangeFavouriteIntent -> changeFavourite(intent)
					}
				}
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

	private suspend fun ProducerScope<RandomFactUiState>.initialRandomFact() {
		val initialFactsFlow = getInitialRandomFactUseCase()
		initialFactsFlow.collect {
			val fact = it.value?.toUiState() ?: FactUiState()
			send(RandomFactUiState(fact = fact))
		}
	}

	private suspend fun ProducerScope<RandomFactUiState>.nextRandomFact() {
		val randomFactResult = getRandomFactUseCase()

		val fact = randomFactResult.value?.toUiState() ?: FactUiState()
		send(RandomFactUiState(fact = fact))
	}

	private suspend fun ProducerScope<RandomFactUiState>.changeFavourite(intent: ChangeFavouriteIntent) {
		val factResult = factRepository.changeFavourite(intent.hash)

		if (factResult.error is NotFoundError)
			onAction(NextRandomFactIntent)

		val fact = factResult.value?.toUiState() ?: FactUiState()
		send(RandomFactUiState(fact = fact))
	}
}
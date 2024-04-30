package kanti.catnfact.feat.facts.screens.randomfact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.NotFoundError
import kanti.catnfact.data.app.AppDataRepository
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.model.settings.SettingsRepository
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.GetRandomFactUseCase
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kanti.catnfact.feat.facts.toUiState
import kanti.catnfact.ui.components.fact.FactUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomFactViewModel @Inject constructor(
	private val appDataRepository: AppDataRepository,
	private val settingsRepository: SettingsRepository,
	private val factRepository: FactRepository,
	private val getRandomFactUseCase: GetRandomFactUseCase,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) : ViewModel() {

	private val mIsLoading = MutableStateFlow(false)
	private val mIsNoConnection = MutableStateFlow(false)

	private val updateState = MutableStateFlow(Any())
	val factUiState: StateFlow<RandomFactUiState> = updateState
		.combine(appDataRepository.lastFactHash) { _, hash -> hash }
		.getData()
		.toUiState()
		.combineWithOtherStates()
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = RandomFactUiState()
		)

	init {
		onNextRandomFact()
	}

	fun onAction(intent: RandomFactIntent) {
		when (intent) {
			is OnNextRandomFactIntent -> onNextRandomFact()
			is OnChangeFavouriteIntent -> onChangeFavourite(intent)
		}
	}

	private fun onNextRandomFact() {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			val randomFactResult = getRandomFactUseCase()

			mIsNoConnection.value = randomFactResult.error is NoConnectionError

			val fact = randomFactResult.value ?: return@launch run {
				mIsLoading.value = false
			}
			appDataRepository.setLastFactHash(fact.hash)
		}
	}

	private fun onChangeFavourite(intent: OnChangeFavouriteIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			val factResult = factRepository.changeFavourite(intent.hash)

			if (factResult.error is NotFoundError)
				onAction(OnNextRandomFactIntent)
			else
				updateState.value = Any()
		}
	}

	private fun Flow<String?>.getData(): Flow<DataResult<Fact, DataError>> {
		return combine(settingsRepository.settings) { hash, settings ->
			if (hash == null)
				return@combine DataResult.Success(Fact())

			val result = getTranslatedFactsUseCase(
				hashes = listOf(hash),
				translateEnabled = settings.autoTranslate
			)
			result.runIfNotError { facts ->
				val fact = facts.getOrNull(0) ?: Fact()
				DataResult.Success(fact)
			}
		}
	}

	private fun Flow<DataResult<Fact, DataError>>.toUiState(): Flow<FactUiState> {
		return map { translatedFact ->
			mIsLoading.value = false
			translatedFact.value?.toUiState() ?: FactUiState()
		}
	}

	private fun Flow<FactUiState>.combineWithOtherStates(): Flow<RandomFactUiState> {
		return combine(this, mIsLoading, mIsNoConnection) { fact, isLoading, isNoConnection ->
			RandomFactUiState(
				fact = fact,
				isLoading = isLoading,
				isNoConnection = isNoConnection
			)
		}
	}
}
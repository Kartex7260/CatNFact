package kanti.catnfact.feat.facts.screens.factslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.domain.fact.FactsPagingQualifier
import kanti.catnfact.feat.facts.toUiState
import kanti.catnfact.ui.components.fact.ExpanderManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactsListViewModel @Inject constructor(
	private val factRepository: FactRepository,
	@FactsPagingQualifier private val factPaging: Pagination<Fact>,
	private val expanderManager: ExpanderManager
) : ViewModel() {

	private val mState = MutableStateFlow(FactsListUiState())
	val factsListUiState: StateFlow<FactsListUiState> = mState.asStateFlow()

	init {
		initialLoad()
		viewModelScope.launch(Dispatchers.Default) {
			factPaging.isNoMore.collect { isNoMore ->
				mState.update {
					it.copy(isLast = isNoMore)
				}
			}
		}
	}

	fun onFactAction(intent: FactIntent) {
		when (intent) {
			is OnChangeExpandIntent -> onChangeExpand(intent)
			is OnChangeFavouriteIntent -> onChangeFavourite(intent)
			is OnRefreshIntent -> onRefresh()
			is OnAppendContentIntent -> onAppendContent()
			is OnReshowIntent -> onReshow()
		}
	}

	private fun onChangeExpand(intent: OnChangeExpandIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			expanderManager.expand(intent.hash)
			mState.update { state ->
				state.copy(
					facts = state.facts.map {
						it.copy(isExpand = expanderManager.isExpand(it.hash))
					}
				)
			}
		}
	}

	private fun onChangeFavourite(intent: OnChangeFavouriteIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			factRepository.changeFavourite(intent.hash)
			showDataFromUseCase()
		}
	}

	private fun onRefresh() {
		viewModelScope.launch(Dispatchers.Default) {
			mState.update { it.copy(isLoading = true) }
			factPaging.load()
			showDataFromUseCase()
		}
	}

	private fun onAppendContent() {
		viewModelScope.launch(Dispatchers.Default) {
			factPaging.load()
			showDataFromUseCase()
		}
	}

	private fun initialLoad() {
		viewModelScope.launch(Dispatchers.Default) {
			mState.update { it.copy(isLoading = true) }

			factPaging.setPageLimit(25)
			factPaging.loadLocal()
			val local = factPaging.getData().value ?: listOf()
			mState.update { state ->
				state.copy(
					facts = local.map {
						it.toUiState(isExpand = expanderManager.isExpand(it.hash))
					}
				)
			}

			factPaging.load()
			showDataFromUseCase()
		}
	}

	private fun onReshow() {
		viewModelScope.launch(Dispatchers.Default) {
			showDataFromUseCase()
		}
	}

	private suspend fun showDataFromUseCase() {
		val factsResult = factPaging.getData()
		mState.update { state ->
			state.copy(
				facts = factsResult.value?.map {
					it.toUiState(isExpand = expanderManager.isExpand(it.hash))
				} ?: state.facts,
				isLoading = false,
				isNoConnection = factsResult.error is NoConnectionError
			)
		}
	}
}
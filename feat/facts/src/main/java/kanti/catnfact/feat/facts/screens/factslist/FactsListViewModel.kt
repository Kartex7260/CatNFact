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
import kanti.catnfact.ui.components.ExpanderManager
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
class FactsListViewModel @Inject constructor(
	private val factRepository: FactRepository,
	@FactsPagingQualifier private val factPaging: Pagination<Fact>,
	private val expanderManager: ExpanderManager
) : ViewModel() {

	private val mIsLoading = MutableStateFlow(false)
	private val mIsNoConnection = MutableStateFlow(false)

	private val updateState = MutableStateFlow(Any())
	val factsListUiState: StateFlow<FactsListUiState> = updateState
		.getData()
		.toUiState()
		.combineWithOtherStates()
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = FactsListUiState()
		)

	init {
		initialLoad()
	}

	fun updateData() {
		factPaging.updateData()
	}

	fun onFactAction(intent: FactIntent) {
		when (intent) {
			is OnChangeExpandIntent -> onChangeExpand(intent)
			is OnChangeFavouriteIntent -> onChangeFavourite(intent)
			is OnRefreshIntent -> onRefresh()
			is OnAppendContentIntent -> onAppendContent()
		}
	}

	private fun onChangeExpand(intent: OnChangeExpandIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			expanderManager.expand(intent.hash)
			updateState.value = Any()
		}
	}

	private fun onChangeFavourite(intent: OnChangeFavouriteIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			factRepository.changeFavourite(intent.hash)
			factPaging.updateData()
		}
	}

	private fun onRefresh() {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			factPaging.load()
		}
	}

	private fun onAppendContent() {
		viewModelScope.launch(Dispatchers.Default) {
			factPaging.load()
		}
	}

	private fun initialLoad() {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			factPaging.loadLocal()
			factPaging.load()
		}
	}

	private fun Flow<Any>.getData(): Flow<List<Fact>> {
		return combine(factPaging.data) { _, data ->
			if (data.isFinally) {
				mIsLoading.value = false
			}
			mIsNoConnection.value = data.data.error is NoConnectionError
			data.data.value ?: listOf()
		}
	}

	private fun Flow<List<Fact>>.toUiState(): Flow<List<FactUiState>> {
		return map { facts ->
			facts.map { fact -> fact.toUiState(expanderManager.isExpand(fact.hash)) }
		}
	}

	private fun Flow<List<FactUiState>>.combineWithOtherStates(): Flow<FactsListUiState> {
		return combine(this, mIsLoading, mIsNoConnection, factPaging.isNoMore) { facts, isLoading, isNoConnection, isNoMore ->
			FactsListUiState(
				facts = facts,
				isLoading = isLoading,
				isNoConnection = isNoConnection,
				isNoMore = isNoMore
			)
		}
	}
}
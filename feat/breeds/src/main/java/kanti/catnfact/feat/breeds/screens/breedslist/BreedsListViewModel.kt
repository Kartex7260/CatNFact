package kanti.catnfact.feat.breeds.screens.breedslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.domain.breed.BreedsPagingQualifier
import kanti.catnfact.feat.breeds.toUiState
import kanti.catnfact.ui.components.ExpanderManager
import kanti.catnfact.ui.components.breed.BreedUiState
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
class BreedsListViewModel @Inject constructor(
	private val breedsRepository: BreedRepository,
	@BreedsPagingQualifier private val breedsPagingManager: Pagination<Breed>,
	private val expanderManager: ExpanderManager
) : ViewModel() {

	private val mIsNoConnection = MutableStateFlow(false)
	private val mIsLoading = MutableStateFlow(false)

	private val updateState = MutableStateFlow(Any())
	val breedsUiState: StateFlow<BreedsListUiState> = updateState
		.getData()
		.toUiState()
		.combineWithOtherStates()
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = BreedsListUiState()
		)

	init {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			breedsPagingManager.loadLocal()
			breedsPagingManager.load()
		}
	}

	fun onAction(intent: BreedsIntent) {
		when (intent) {
			is OnAppendContentIntent -> onAppendContent()
			is OnRefreshIntent -> onRefreshIntent()
			is OnChangeExpandIntent -> onChangeExpand(intent)
			is OnChangeFavouriteIntent -> onChangeFavourite(intent)
		}
	}

	private fun onAppendContent() {
		viewModelScope.launch(Dispatchers.Default) {
			breedsPagingManager.load()
		}
	}

	private fun onRefreshIntent() {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			breedsPagingManager.load()
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
			breedsRepository.changeFavourite(intent.hash)
			breedsPagingManager.updateData()
		}
	}

	private fun Flow<Any>.getData(): Flow<List<Breed>> {
		return combine(breedsPagingManager.data) { _, data ->
			if (data.isFinally) {
				mIsLoading.value = false
			}
			mIsNoConnection.value = data.data.error is NoConnectionError
			data.data.value ?: listOf()
		}
	}

	private fun Flow<List<Breed>>.toUiState(): Flow<List<BreedUiState>> {
		return map { breads ->
			breads.map { bread ->
				bread.toUiState(isExpand = expanderManager.isExpand(bread.hash))
			}
		}
	}

	private fun Flow<List<BreedUiState>>.combineWithOtherStates(): Flow<BreedsListUiState> {
		return combine(this, mIsLoading, mIsNoConnection, breedsPagingManager.isNoMore) { data, isLoading, isNoConnection, isNoMore ->
			BreedsListUiState(
				breeds = data,
				isLoading = isLoading,
				isNoConnection = isNoConnection,
				isNoMore = isNoMore
			)
		}
	}
}
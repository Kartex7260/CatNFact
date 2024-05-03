package kanti.catnfact.feat.favourite.screens.breedslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.model.breed.BreedRepository
import kanti.catnfact.data.paging.Pagination
import kanti.catnfact.domain.breed.FavouriteBreedsPagingQualifier
import kanti.catnfact.feat.favourite.toUiState
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
class FavouriteBreedsListViewModel @Inject constructor(
	private val breedRepository: BreedRepository,
	@FavouriteBreedsPagingQualifier private val pagination: Pagination<Breed>,
	private val expanderManager: ExpanderManager
) : ViewModel() {

	private val mIsLoading = MutableStateFlow(false)
	private val mIsNoConnection = MutableStateFlow(false)

	private val updateState = MutableStateFlow(Any())
	val breedsUiState: StateFlow<FavouriteBreedsListUiState> = updateState
		.getData()
		.toUiState()
		.combineWithOtherStates()
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = FavouriteBreedsListUiState()
		)

	init {
		init()
	}

	fun onScreenAction(intent: ScreenIntent) {
		when (intent) {
			is OnStartIntent -> onStart()
		}
	}

	fun onBreedAction(intent: BreedIntent) {
		when (intent) {
			is OnChangeFavouriteIntent -> onChangeFavourite(intent)
			is OnChangeExpandIntent -> onChangeExpand(intent)
			is OnRefreshIntent -> onRefresh()
			is OnAppendContentIntent -> onAppendContent()
		}
	}

	private fun init() {
		viewModelScope.launch(Dispatchers.Default) {
			mIsLoading.value = true
			pagination.load()
		}
	}

	private fun onStart() {
		if (breedsUiState.value.isNoConnection)
			onBreedAction(OnRefreshIntent)
		else
			pagination.updateData()
	}

	private fun onChangeFavourite(intent: OnChangeFavouriteIntent) {
		viewModelScope.launch(Dispatchers.Default) {
			breedRepository.changeFavourite(hash = intent.hash)
			pagination.updateData()
		}
	}

	private fun onChangeExpand(intent: OnChangeExpandIntent) {
		expanderManager.expand(hash = intent.hash)
		updateState.value = Any()
	}

	private fun onRefresh() {
		viewModelScope.launch {
			mIsLoading.value = true
			pagination.load()
		}
	}

	private fun onAppendContent() {
		viewModelScope.launch {
			pagination.load()
		}
	}

	private fun Flow<Any>.getData(): Flow<List<Breed>> {
		return combine(pagination.data) { _, data ->
			if (data.isFinally) {
				mIsLoading.value = true
			}
			mIsNoConnection.value = data.data.error is NoConnectionError
			data.data.value ?: listOf()
		}
	}

	private fun Flow<List<Breed>>.toUiState(): Flow<List<BreedUiState>> {
		return map { breeds ->
			breeds.map { breed ->
				breed.toUiState(isExpand = expanderManager.isExpand(hash = breed.hash))
			}
		}
	}

	private fun Flow<List<BreedUiState>>.combineWithOtherStates(): Flow<FavouriteBreedsListUiState> {
		return combine(this, mIsLoading, mIsNoConnection, pagination.isNoMore) {
			breeds, isLoading, isNoConnection, isNoMore ->
			FavouriteBreedsListUiState(
				breeds = breeds,
				isLoading = isLoading,
				isNoConnection = isNoConnection,
				isNoMore = isNoMore
			)
		}
	}
}
package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPagingFactsListUseCase @Inject constructor(
	private val factRepository: FactRepository
) {

	private val mutex = Mutex()

	private var mPageLimit = 25
	private var currentPage = 1

	private var isLocalData: Boolean = false
	private var hashes: MutableList<String>? = null
	private var dataError: DataError? = null

	private val mIsLast = MutableStateFlow(false)
	val isLast: StateFlow<Boolean> = mIsLast.asStateFlow()

	suspend operator fun invoke(): DataResult<List<Fact>, DataError> {
		return withContext(Dispatchers.Default) {
			if (dataError != null)
				DataResult.Error(dataError!!)
			else if (hashes != null) {
				val facts = factRepository.getLocalFacts(hashes = hashes!!)
				DataResult.Success(
					hashes?.map { hash -> facts.first { it.hash == hash } } ?: facts
				)
			} else
				DataResult.Error(ValueIsNullError())
		}
	}

	suspend fun setPageSize(limit: Int) { mutex.withLock { mPageLimit = limit } }

	suspend fun loadLocal() {
		withContext(Dispatchers.Default) {
			val localData = factRepository.getLocalFactsHashes(limit = mPageLimit)
			mutex.withLock {
				currentPage = 1

				isLocalData = true
				hashes = localData.toMutableList()
				dataError = null

				mIsLast.value = false
			}
		}
	}

	suspend fun load() {
		if (mIsLast.value)
			return
		withContext(Dispatchers.Default) {
			val remoteResult = factRepository.loadFacts(page = currentPage, limit = mPageLimit)
			if (remoteResult.value?.size == 0)
				mIsLast.value = true
			mutex.withLock {
				currentPage++

				if (isLocalData) {
					hashes = remoteResult.value?.toMutableList()
					isLocalData = false
				} else {
					if (hashes != null) {
						remoteResult.value?.let {
							hashes!!.addAll(it)
						}
					} else
						hashes = remoteResult.value?.toMutableList()
				}

				dataError = remoteResult.error
			}
		}
	}
}
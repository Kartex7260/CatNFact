package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kanti.catnfact.data.runIfNotError
import kanti.catnfact.domain.fact.translated.GetTranslatedFactsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPagingFactsListUseCase @Inject constructor(
	private val factRepository: FactRepository,
	private val getTranslatedFactsUseCase: GetTranslatedFactsUseCase
) {

	private val mutex = Mutex()

	private var mPageLimit = 25
	private var currentPage = 1

	private var isLocalHashes: Boolean = false
	private var hashes: MutableList<String>? = null
	private var dataError: DataError? = null

	private val mIsLast = MutableStateFlow(false)
	val isLast: StateFlow<Boolean> = mIsLast.asStateFlow()

	suspend operator fun invoke(): DataResult<List<Fact>, DataError> {
		return withContext(Dispatchers.Default) {
			mutex.withLock {
				if (dataError != null) {
					DataResult.Error(
						error = dataError!!,
						value = hashes?.let { hashes ->
							val translatedFacts = getTranslatedFactsUseCase(hashes).value ?: return@let null
							hashes.map { hash -> translatedFacts.first { it.hash == hash } }
						}
					)
				} else if (hashes != null) {
					val translatedFacts = getTranslatedFactsUseCase(hashes!!)
					translatedFacts.runIfNotError { facts ->
						DataResult.Success(
							value = hashes!!.map { hash -> facts.first { it.hash == hash } }
						)
					}
				} else
					DataResult.Error(ValueIsNullError())
			}
		}
	}

	suspend fun setPageSize(limit: Int) { mutex.withLock { mPageLimit = limit } }

	suspend fun loadLocal() {
		withContext(Dispatchers.Default) {
			val localData = factRepository.getLocalFactsHashes(limit = mPageLimit)
			mutex.withLock {
				currentPage = 1

				isLocalHashes = true
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
			if (remoteResult.value?.size == 0) {
				mIsLast.value = true
				return@withContext
			}
			mutex.withLock {
				val error = remoteResult.error
				dataError = error
				if (error != null) {
					return@withLock
				}

				val value = remoteResult.value ?: return@withLock

				if (isLocalHashes) {
					hashes = value.toMutableList()
					isLocalHashes = false
				} else {
					if (hashes != null) {
						hashes!!.addAll(value)
					} else
						hashes = value.toMutableList()
				}

				currentPage++
			}
		}
	}
}
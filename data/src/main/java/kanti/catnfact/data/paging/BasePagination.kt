package kanti.catnfact.data.paging

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BasePagination<DataType>(
	private val pageLimit: Int,
	private val context: CoroutineContext = Dispatchers.Default
) : Pagination<DataType> {

	private val mutex = Mutex()

	private var isLocalData: Boolean = false
	private var page = 0

	private var mData: MutableList<DataType>? = null
	private var error: DataError? = null

	private val mIsNoMore = MutableStateFlow(false)
	override val isNoMore: Flow<Boolean> = mIsNoMore.asStateFlow()

	private val updateState = MutableStateFlow(Any())
	override val data: Flow<DataResult<List<DataType>, DataError>> = updateState
		.map { getCurrentData() }

	private suspend fun getCurrentData(): DataResult<List<DataType>, DataError> = withContext(context) {
		mutex.withLock {
			val error = error
			if (error != null) {
				return@withContext DataResult.Error(error, mData)
			}

			val data = mData ?: return@withContext DataResult.Error(ValueIsNullError("Data"))
			DataResult.Success(data)
		}
	}

	override fun updateData() {
		updateState.value = Any()
	}

	override suspend fun loadLocal() = withContext(context) {
		mutex.withLock {
			val localData = loadLocal0(limit = pageLimit)

			isLocalData = true
			page = 1

			mData = localData.toMutableList()
			error = null

			mIsNoMore.value = false
			updateState.value = Any()
		}
	}

	override suspend fun load(): Unit = withContext(context) {
		if (mIsNoMore.value)
			return@withContext

		mutex.withLock {
			val remoteDataResult = load0(page = page, limit = pageLimit)

			error = remoteDataResult.error
			if (remoteDataResult.error != null)
				return@withLock

			val remoteData = remoteDataResult.value ?: return@withLock

			if (remoteData.isEmpty()) {
				mIsNoMore.value = true
				return@withLock
			}

			if (isLocalData) {
				mData = remoteData.toMutableList()
				isLocalData = false
			} else {
				if (mData == null) {
					mData = remoteData.toMutableList()
				} else {
					mData!!.addAll(remoteData)
				}
			}

			page++
		}
		updateState.value = Any()
	}

	protected abstract suspend fun loadLocal0(limit: Int): List<DataType>

	protected abstract suspend fun load0(page: Int, limit: Int): DataResult<List<DataType>, DataError>
}
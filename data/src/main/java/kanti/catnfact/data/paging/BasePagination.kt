package kanti.catnfact.data.paging

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.ValueIsNullError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BasePagination<DataType>(
	private val context: CoroutineContext = Dispatchers.Default
) : Pagination<DataType> {

	private val mutex = Mutex()

	private var isLocalData: Boolean = false
	private var page = 0
	private var pageLimit: Int = 25

	private var data: MutableList<DataType>? = null
	private var error: DataError? = null

	private val mIsNoMore = MutableStateFlow(false)
	override val isNoMore = mIsNoMore.asStateFlow()

	override suspend fun setPageLimit(limit: Int) {
		mutex.withLock { pageLimit = limit }
	}

	override suspend fun getData(): DataResult<List<DataType>, DataError> = withContext(context) {
		mutex.withLock {
			val error = error
			if (error != null) {
				return@withContext DataResult.Error(error, data)
			}

			val data = data ?: return@withContext DataResult.Error(ValueIsNullError("Data"))
			DataResult.Success(data)
		}
	}

	override suspend fun loadLocal() = withContext(context) {
		mutex.withLock {
			val localData = loadLocal0(limit = pageLimit)

			isLocalData = true
			page = 1

			data = localData.toMutableList()
			error = null

			mIsNoMore.value = false
		}
	}

	override suspend fun load(): Unit = withContext(context) {
		if (mIsNoMore.value)
			return@withContext

		mutex.withLock {
			val remoteDataResult = load0(page = page, limit = pageLimit)

			error = remoteDataResult.error
			if (remoteDataResult.error != null)
				return@withContext

			val remoteData = remoteDataResult.value ?: return@withContext

			if (remoteData.isEmpty()) {
				mIsNoMore.value = true
				return@withContext
			}

			if (isLocalData) {
				data = remoteData.toMutableList()
				isLocalData = false
			} else {
				if (data == null) {
					data = remoteData.toMutableList()
				} else {
					data!!.addAll(remoteData)
				}
			}

			page++
		}
	}

	protected abstract suspend fun loadLocal0(limit: Int): List<DataType>

	protected abstract suspend fun load0(page: Int, limit: Int): DataResult<List<DataType>, DataError>
}
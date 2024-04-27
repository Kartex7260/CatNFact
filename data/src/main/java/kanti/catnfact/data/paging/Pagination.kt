package kanti.catnfact.data.paging

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kotlinx.coroutines.flow.StateFlow

interface Pagination<T> {

	val isNoMore: StateFlow<Boolean>

	suspend fun setPageLimit(limit: Int)

	suspend fun getData(): DataResult<List<T>, DataError>

	suspend fun loadLocal()

	suspend fun load()
}
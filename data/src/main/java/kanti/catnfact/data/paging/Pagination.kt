package kanti.catnfact.data.paging

import kotlinx.coroutines.flow.Flow

interface Pagination<DataType> {

	val data: Flow<PagingResult<DataType>>

	val isNoMore: Flow<Boolean>

	fun updateData()

	suspend fun loadLocal()

	suspend fun load()
}
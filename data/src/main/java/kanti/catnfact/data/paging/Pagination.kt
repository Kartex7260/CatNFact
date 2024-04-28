package kanti.catnfact.data.paging

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kotlinx.coroutines.flow.Flow

interface Pagination<DataType> {

	val data: Flow<DataResult<List<DataType>, DataError>>

	val isNoMore: Flow<Boolean>

	fun updateData()

	suspend fun loadLocal()

	suspend fun load()
}
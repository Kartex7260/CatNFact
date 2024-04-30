package kanti.catnfact.data.paging

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult

data class PagingResult<DataType>(
	val data: DataResult<List<DataType>, DataError> = DataResult.Success(listOf()),
	val isFinally: Boolean = false
)

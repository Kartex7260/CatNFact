package kanti.catnfact.domain.fact

import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.model.fact.FactRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class GetPagingFactsListUseCase @Inject constructor(
	private val factRepository: FactRepository
) {

	private val mIsLast = MutableSharedFlow<Any>()
	val isLast: SharedFlow<Any> = mIsLast.asSharedFlow()

	suspend operator fun invoke(): DataResult<List<Fact>, DataError> {
		TODO("Реализовать")
	}

	suspend fun loadLocal() {}

	suspend fun load() {}
}
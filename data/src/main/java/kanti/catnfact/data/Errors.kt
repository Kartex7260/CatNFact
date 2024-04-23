package kanti.catnfact.data

sealed interface DataError {

	val message: String?
	val throwable: Throwable?
}

sealed interface LocalError : DataError

data class NotFoundError(
	override val message: String? = null,
	override val throwable: Throwable? = null
): LocalError

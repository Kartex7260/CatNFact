package kanti.catnfact.data

sealed interface DataError {

	val message: String?
	val throwable: Throwable?
}

data class ValueIsNullError(
	override val message: String? = null,
	override val throwable: Throwable? = null
) : DataError, LocalError, RemoteError

data class UnexpectedError(
	override val message: String? = null,
	override val throwable: Throwable? = null
) : DataError, LocalError, RemoteError

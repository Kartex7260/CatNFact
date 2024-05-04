package kanti.catnfact.data

sealed interface RemoteError : DataError

data class NoConnectionError(
	override val message: String? = null,
	override val throwable: Throwable? = null
) : RemoteError

data class BadRequestError(
	override val message: String? = null,
	override val throwable: Throwable? = null,
) : RemoteError
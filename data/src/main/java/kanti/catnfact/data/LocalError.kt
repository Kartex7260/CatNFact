package kanti.catnfact.data

sealed interface LocalError : DataError

data class NotFoundError(
	override val message: String? = null,
	override val throwable: Throwable? = null
): LocalError
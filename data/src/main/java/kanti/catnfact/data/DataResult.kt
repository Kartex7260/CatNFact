package kanti.catnfact.data

sealed class DataResult<Value, Error : DataError>(
	val value: Value? = null,
	val error: Error? = null
) {

	class Success<Value, Error : DataError>(
		value: Value,
		error: Error?,
	) : DataResult<Value, Error>(value, error)

	class Error<Value, Error : DataError>(
		error: Error,
		value: Value? = null
	) : DataResult<Value, Error>(value, error)
}


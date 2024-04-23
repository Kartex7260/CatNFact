package kanti.catnfact.data

sealed class DataResult<Value, Error : DataError>(
	val value: Value? = null,
	val error: Error? = null
) {

	class Success<Value, Error : DataError>(
		value: Value,
		error: Error? = null,
	) : DataResult<Value, Error>(value, error)

	class Error<Value, Error : DataError>(
		error: Error,
		value: Value? = null
	) : DataResult<Value, Error>(value, error)
}

suspend fun <ValueInput, ValueOutput, Error : DataError> DataResult<ValueInput, Error>.runIfNotError(
	run: suspend (ValueInput) -> DataResult<ValueOutput, DataError>
) : DataResult<ValueOutput, DataError> {
	val error = error
	if (error != null) {
		return DataResult.Error(error)
	}

	val value = value ?: return DataResult
		.Error(ValueIsNullError())

	return run(value)
}


package kanti.catnfact.data.model.fact.datasource.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.RemoteError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.retrofit.fact.FactService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.security.MessageDigest
import javax.inject.Inject

class FactRetrofitDataSource @Inject constructor(
	private val factService: FactService,
	private val digest: MessageDigest
) : FactRemoteDataSource {

	@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
	override suspend fun getRandomFact(): DataResult<Fact, RemoteError> {
		return withContext(Dispatchers.Default) {
			try {
				val remoteFact = factService.getRandomFact()
				DataResult.Success(remoteFact.toFact(digest))
			} catch (ex: IOException) {
				DataResult.Error(NoConnectionError(ex.message, ex))
			} catch (ex: HttpException) {
				DataResult.Error(UnexpectedError(ex.message, ex))
			}
		}
	}
}
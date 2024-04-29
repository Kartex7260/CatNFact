package kanti.catnfact.data.model.breed.datasource.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import kanti.catnfact.data.DataError
import kanti.catnfact.data.DataResult
import kanti.catnfact.data.NoConnectionError
import kanti.catnfact.data.UnexpectedError
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.retrofit.catfact.breed.BreedService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.security.MessageDigest
import javax.inject.Inject

class BreedRetrofitDataSource @Inject constructor(
	private val breedService: BreedService,
	private val digest: MessageDigest
) : BreedRemoteDataSource {

	@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
	override suspend fun getBreeds(page: Int, limit: Int): DataResult<List<Breed>, DataError> {
		return withContext(Dispatchers.Default) {
			try {
				val breedsRemote = breedService.getBreeds(page = page, limit = limit)
				DataResult.Success(
					value = breedsRemote.data.map { it.toBreed(digest) }
				)
			} catch (ex: IOException) {
				DataResult.Error(NoConnectionError(ex.message, ex))
			} catch (ex: HttpException) {
				DataResult.Error(UnexpectedError(ex.message, ex))
			}
		}
	}
}
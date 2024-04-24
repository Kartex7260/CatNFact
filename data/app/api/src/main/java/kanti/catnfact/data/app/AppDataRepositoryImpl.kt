package kanti.catnfact.data.app

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
	private val localDataSource: AppDataLocalDataSource
) : AppDataRepository {

	override val lastFactHash: Flow<String?> = localDataSource.lastFactHash

	override suspend fun setLastFactHash(hash: String?) {
		localDataSource.setLastFactHash(hash)
	}
}
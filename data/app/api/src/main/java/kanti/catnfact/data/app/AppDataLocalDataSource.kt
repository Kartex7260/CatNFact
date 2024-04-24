package kanti.catnfact.data.app

import kotlinx.coroutines.flow.Flow

interface AppDataLocalDataSource {

	val lastFactHash: Flow<String?>

	suspend fun setLastFactHash(hash: String?)
}
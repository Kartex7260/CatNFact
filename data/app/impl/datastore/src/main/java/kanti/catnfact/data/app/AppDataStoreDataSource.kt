package kanti.catnfact.data.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "appData"
private const val lastFactKeyName = "lastFact"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)

class AppDataStoreDataSource @Inject constructor(
	@ApplicationContext private val context: Context
) : AppDataLocalDataSource {

	private val lastFactKey = stringPreferencesKey(name = lastFactKeyName)

	override val lastFactHash: Flow<String?> = context.dataStore.data
		.map { preferences ->
			preferences[lastFactKey]
		}

	override suspend fun setLastFactHash(hash: String?) {
		context.dataStore.edit { preferences ->
			if (hash == null) {
				preferences.remove(lastFactKey)
			} else {
				preferences[lastFactKey] = hash
			}
		}
	}
}
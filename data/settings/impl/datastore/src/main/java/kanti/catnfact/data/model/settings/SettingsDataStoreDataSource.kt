package kanti.catnfact.data.model.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "settings"

private const val darkModeKeyName = "darkMode"
private const val colorStyleKeyName = "colorStyle"
private const val autoTranslateKeyName = "autoTranslate"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)

class SettingsDataStoreDataSource @Inject constructor(
	@ApplicationContext private val context: Context
) : SettingsLocalDataSource {

	private val darkModeKey = stringPreferencesKey(name = darkModeKeyName)
	private val colorStyleKey = stringPreferencesKey(name = colorStyleKeyName)
	private val autoTranslateKey = booleanPreferencesKey(name = autoTranslateKeyName)

	override val settingsData: Flow<LocalSettingsData> = context.dataStore.data
		.map { preferences ->
			LocalSettingsData(
				darkMode = preferences[darkModeKey]?.let { DarkMode.valueOf(it) },
				colorStyle = preferences[colorStyleKey]?.let { ColorStyle.valueOf(it) },
				autoTranslate = preferences[autoTranslateKey]
			)
		}

	override suspend fun setDarkMode(darkMode: DarkMode) {
		context.dataStore.edit { preferences ->
			preferences[darkModeKey] = darkMode.name
		}
	}

	override suspend fun setColorStyle(colorStyle: ColorStyle) {
		context.dataStore.edit { preferences ->
			preferences[colorStyleKey] = colorStyle.name
		}
	}

	override suspend fun setAutoTranslate(enabled: Boolean) {
		context.dataStore.edit { preferences ->
			preferences[autoTranslateKey] = enabled
		}
	}
}
package kanti.catnfact.data.model.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "settings"

private const val darkModeKeyName = "darkMode"
private const val colorStyleKeyName = "colorStyle"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)

class SettingsRepositoryImpl @Inject constructor(
	@ApplicationContext private val context: Context
) : SettingsRepository {

	private val darkModeKey = stringPreferencesKey(name = darkModeKeyName)
	private val colorStyleKey = stringPreferencesKey(name = colorStyleKeyName)

	override val settings: Flow<SettingsData> = context.dataStore.data
		.map { preferences ->
			var returnNull = false

			val darkMode = preferences[darkModeKey].run {
				if (this == null) {
					returnNull = true
					setDefaultDarkMode()
					DarkMode.AsSystem
				} else {
					DarkMode.valueOf(this)
				}
			}

			val colorStyle = preferences[colorStyleKey].run {
				if (this == null) {
					returnNull = true
					setDefaultColorStyle()
					ColorStyle.CatNFact
				} else {
					ColorStyle.valueOf(this)
				}
			}

			if (returnNull)
				return@map null
			SettingsData(
				darkMode = darkMode,
				colorStyle = colorStyle
			)
		}
		.filterNotNull()

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
}
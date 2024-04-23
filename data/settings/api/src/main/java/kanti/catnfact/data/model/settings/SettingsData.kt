package kanti.catnfact.data.model.settings

data class SettingsData(
	val darkMode: DarkMode = DarkMode.AsSystem
)

enum class DarkMode {

	Light, Dark, AsSystem
}

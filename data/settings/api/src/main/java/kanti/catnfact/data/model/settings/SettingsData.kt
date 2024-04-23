package kanti.catnfact.data.model.settings

data class SettingsData(
	val darkMode: DarkMode = DarkMode.AsSystem,
	val colorStyle: ColorStyle = ColorStyle.CatNFact
)

enum class DarkMode {

	Light, Dark, AsSystem
}

enum class ColorStyle {

	CatNFact, AsSystem
}

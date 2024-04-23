package kanti.catnfact

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.settings.MainSettingsScreen
import kanti.catnfact.ui.components.settings.DarkModeUiState
import kanti.catnfact.ui.theme.CatNFactTheme
import kanti.catnfact.ui.theme.ColorStyle

@Composable
fun MainContent(
	darkMode: DarkModeUiState
) {
	CatNFactTheme(
		darkTheme = when (darkMode) {
			DarkModeUiState.Light -> false
			DarkModeUiState.Dark -> true
			DarkModeUiState.AsSystem -> isSystemInDarkTheme()
		},
		colorStyle = ColorStyle.CatNFact
	) {
		val navController = rememberNavController()
		NavHost(
			navController = navController,
			startDestination = Destinations.SETTINGS
		) {
			composable(
				route = Destinations.SETTINGS
			) {
				MainSettingsScreen(
					navController = navController
				)
			}
		}
	}
}

object Destinations {

	const val SETTINGS = "settings"
}
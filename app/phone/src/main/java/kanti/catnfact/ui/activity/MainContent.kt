package kanti.catnfact.ui.activity

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.settings.MainSettingsScreen
import kanti.catnfact.ui.components.settings.DarkModeUiState
import kanti.catnfact.ui.main.MainFeatures
import kanti.catnfact.ui.theme.CatNFactTheme
import kanti.catnfact.ui.theme.ColorStyle

@Composable
fun MainContent(
	darkMode: DarkModeUiState,
	colorStyle: ColorStyle
) {
	CatNFactTheme(
		darkTheme = when (darkMode) {
			DarkModeUiState.Light -> false
			DarkModeUiState.Dark -> true
			DarkModeUiState.AsSystem -> isSystemInDarkTheme()
		},
		colorStyle = colorStyle
	) {
		val navController = rememberNavController()
		Surface(
			modifier = Modifier.fillMaxSize()
		) {
			NavHost(
				navController = navController,
				startDestination = Destinations.MAIN_FEATURES
			) {
				composable(
					route = Destinations.SETTINGS
				) {
					MainSettingsScreen(
						navController = navController
					)
				}

				composable(
					route = Destinations.MAIN_FEATURES
				) {
					MainFeatures(
						toSettings = {
							navController.navigate(route = Destinations.SETTINGS)
						}
					)
				}
			}
		}
	}
}

object Destinations {

	const val SETTINGS = "settings"
	const val MAIN_FEATURES = "main_features"
}
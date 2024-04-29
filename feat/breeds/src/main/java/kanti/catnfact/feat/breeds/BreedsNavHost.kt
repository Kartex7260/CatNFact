package kanti.catnfact.feat.breeds

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.breeds.screens.breedslist.BreedsListScreen

@Composable
fun BreedsNavHost(
	toSettings: () -> Unit
) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = BreedsDestinations.BREEDS_LIST
	) {
		composable(
			route = BreedsDestinations.BREEDS_LIST
		) {
			BreedsListScreen(
				toSettings = toSettings
			)
		}
	}
}

object BreedsDestinations {

	const val BREEDS_LIST = "fact_list"
}
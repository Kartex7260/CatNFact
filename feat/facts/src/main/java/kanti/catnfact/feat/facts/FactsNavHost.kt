package kanti.catnfact.feat.facts

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.facts.screens.factslist.FactsListScreen
import kanti.catnfact.feat.facts.screens.randomfact.RandomFactScreen

@Composable
fun FactsNavHost(
	toSettings: () -> Unit = {}
) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = FactsDestinations.RANDOM_FACT
	) {
		composable(
			route = FactsDestinations.RANDOM_FACT
		) {
			RandomFactScreen(
				toSettings = toSettings,
				toFactList = { navController.navigate(route = FactsDestinations.FACTS_LIST) }
			)
		}

		composable(
			route = FactsDestinations.FACTS_LIST
		) {
			FactsListScreen(
				onBack = { navController.popBackStack() },
				toSettings = toSettings
			)
		}
	}
}

object FactsDestinations {

	const val RANDOM_FACT = "random_fact"
	const val FACTS_LIST = "fact_list"
}

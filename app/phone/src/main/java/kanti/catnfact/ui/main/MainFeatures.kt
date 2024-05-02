package kanti.catnfact.ui.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.R
import kanti.catnfact.feat.breeds.BreedsNavHost
import kanti.catnfact.feat.facts.FactsNavHost
import kanti.catnfact.feat.favourite.FavouriteScreen

@Composable
fun MainFeatures(
	toSettings: () -> Unit = {}
) {
	val navController = rememberNavController()
	Column {
		NavHost(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			navController = navController,
			startDestination = FeatDestinations.FACTS,
			enterTransition = { EnterTransition.None },
			exitTransition = { ExitTransition.None },
			popEnterTransition = { EnterTransition.None },
			popExitTransition = { ExitTransition.None }
		) {
			composable(
				route = FeatDestinations.FACTS
			) {
				FactsNavHost(
					toSettings = toSettings
				)
			}

			composable(
				route = FeatDestinations.BREEDS
			) {
				BreedsNavHost(
					toSettings = toSettings
				)
			}

			composable(
				route = FeatDestinations.FAVOURITES
			) {
				FavouriteScreen(
					onNavigateToSettings = toSettings
				)
			}
		}
		NavigationBar {
			navItems.forEach { navItem ->
				val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
				val currentRoute = currentNavBackStackEntry?.destination?.route

				NavigationBarItem(
					selected = currentRoute == navItem.route,
					onClick = onClick@{
						if (currentRoute == navItem.route)
							return@onClick
						navController.navigate(route = navItem.route) {
							currentRoute?.let {
								popUpTo(route = it) {
									inclusive = true
									saveState = true
								}
							}
							restoreState = true
						}
					},
					icon = {
						Icon(
							painter = painterResource(id = navItem.iconId),
							contentDescription = null
						)
					},
					label = {
						Text(text = stringResource(id = navItem.titleId))
					}
				)
			}
		}
	}
}

private val navItems = listOf(
	NavItem(
		route = FeatDestinations.FACTS,
		titleId = R.string.facts,
		iconId = R.drawable.fact_icon
	),
	NavItem(
		route = FeatDestinations.BREEDS,
		titleId = R.string.breeds,
		iconId = R.drawable.breeds_icon
	),
	NavItem(
		route = FeatDestinations.FAVOURITES,
		titleId = R.string.favourites,
		iconId = R.drawable.star
	)
)

private data class NavItem(
	val route: String,
	val titleId: Int,
	val iconId: Int
)

object FeatDestinations {

	const val FACTS = "facts"
	const val BREEDS = "breeds"
	const val FAVOURITES = "favourites"
}

@Preview
@Composable
private fun PreviewMainFeatures() {
	MainFeatures()
}
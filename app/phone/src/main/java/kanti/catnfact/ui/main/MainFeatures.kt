package kanti.catnfact.ui.main

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
import kanti.catnfact.feat.facts.FactsNavHost

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
			startDestination = FeatDestinations.FACTS
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
			}

			composable(
				route = FeatDestinations.FAVOURITES
			) {
			}
		}
		NavigationBar {
			navItems.forEach {
				val currentDestination by navController.currentBackStackEntryAsState()

				NavigationBarItem(
					selected = currentDestination?.destination?.route == it.route,
					onClick = {
						navController.navigate(route = it.route) {
							popUpTo(route = it.route) {
								inclusive = true
								saveState = true
							}
						}
					},
					icon = {
						Icon(
							painter = painterResource(id = it.iconId),
							contentDescription = null
						)
					},
					label = {
						Text(text = stringResource(id = it.titleId))
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
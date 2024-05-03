package kanti.catnfact.feat.favourite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.feat.favourite.screens.breedslist.FavouriteBreedsListScreen
import kanti.catnfact.feat.favourite.screens.factslist.FavouriteFactsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
	onNavigateToSettings: () -> Unit
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text(text = stringResource(id = R.string.favourites)) },
				actions = {
					var expandMenu by rememberSaveable { mutableStateOf(false) }

					IconButton(onClick = { expandMenu = !expandMenu }) {
						Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
					}

					DropdownMenu(
						expanded = expandMenu,
						onDismissRequest = { expandMenu = false },
						offset = DpOffset(x = (-10).dp, 0.dp)
					) {
						DropdownMenuItem(
							text = { Text(text = stringResource(id = R.string.settings)) },
							onClick = {
								expandMenu = false
								onNavigateToSettings()
							}
						)
					}
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier.padding(paddingValues)
		) {
			val navController = rememberNavController()
			val currentRoute = navController.currentBackStackEntryAsState()
				.value?.destination?.route

			PrimaryTabRow(
				selectedTabIndex = run {
					val index = tabs.indexOfFirst { it.route == currentRoute }
					if (index == -1)
						return@run 1
					index
				},
				divider = { HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant) }
			) {
				tabs.forEach { tabContent ->
					Tab(
						selected = currentRoute == tabContent.route,
						onClick = {
							navController.navigate(route = tabContent.route) {
								currentRoute?.let {
									popUpTo(route = it) {
										inclusive = true
										saveState = true
									}
								}
								restoreState = true
							}
						},
						text = { Text(text = stringResource(id = tabContent.stringId)) },
						unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			NavHost(
				navController = navController,
				startDestination = FavouriteDestinations.FACTS
			) {
				composable(route = FavouriteDestinations.FACTS) {
					FavouriteFactsListScreen(modifier = Modifier.fillMaxSize())
				}
				composable(route = FavouriteDestinations.BREEDS) {
					FavouriteBreedsListScreen(modifier = Modifier.fillMaxSize())
				}
			}
		}
	}
}

private object FavouriteDestinations {

	const val FACTS = "facts"
	const val BREEDS = "breeds"
}

private data class TabContent(
	val route: String,
	val stringId: Int
)

private val tabs = listOf(
	TabContent(
		route = FavouriteDestinations.FACTS,
		stringId = R.string.facts
	),
	TabContent(
		route = FavouriteDestinations.BREEDS,
		stringId = R.string.breeds
	)
)
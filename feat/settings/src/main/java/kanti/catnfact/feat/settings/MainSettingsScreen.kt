package kanti.catnfact.feat.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.ui.components.settings.DarkModeItem
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun MainSettingsScreen(
	navController: NavController = rememberNavController()
) {
	MainSettingsContent(
		uiState = UiSettingsUiState(),
		onScreenAction = {
			navController.popBackStack()
		},
		onUiSettingsAction = {}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsContent(
	uiState: UiSettingsUiState,
	onScreenAction: (ScreenIntent) -> Unit,
	onUiSettingsAction: (UiSettingsIntent) -> Unit
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			TopAppBar(
				title = {
					Text(text = stringResource(id = R.string.settings))
				},
				navigationIcon = {
					IconButton(onClick = { onScreenAction(OnBackIntent) }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = null
						)
					}
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { paddingValues ->
		LazyColumn(
			modifier = Modifier.padding(paddingValues)
		) {
			item {
				DarkModeItem(
					state = uiState.darkMode,
					onChangeDarkMode = {
						onUiSettingsAction(DarkModeIntent(it))
					}
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewMainSettingsContent() {
	CatNFactTheme {
		MainSettingsContent(
			uiState = UiSettingsUiState(),
			onScreenAction = {},
			onUiSettingsAction = {}
		)
	}
}
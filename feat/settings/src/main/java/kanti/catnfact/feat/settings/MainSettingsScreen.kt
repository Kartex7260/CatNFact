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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.catnfact.ui.components.settings.AutoTranslateItem
import kanti.catnfact.ui.components.settings.ColorStyleItem
import kanti.catnfact.ui.components.settings.ColorStyleItemDefaults
import kanti.catnfact.ui.components.settings.DarkModeItem
import kanti.catnfact.ui.components.settings.DarkModeItemDefaults
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun MainSettingsScreen(
	navController: NavController = rememberNavController()
) {
	val viewModel = hiltViewModel<MainSettingsViewModel>()
	MainSettingsContent(
		uiState = viewModel.uiSettings.collectAsState().value,
		onScreenAction = { intent ->
			when (intent) {
				OnBackIntent -> navController.popBackStack()
			}
		},
		onUiSettingsAction = { intent ->
			viewModel.onUiSettingsAction(intent)
		}
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
					strings = DarkModeItemDefaults.strings(
						darkMode = stringResource(id = R.string.set_dark_mode),
						light = stringResource(id = R.string.light),
						dark = stringResource(id = R.string.dark),
						asSystem = stringResource(id = R.string.as_system)
					),
					onChangeDarkMode = {
						onUiSettingsAction(SetDarkModeIntent(it))
					}
				)
			}

			item {
				ColorStyleItem(
					state = uiState.colorStyle,
					strings = ColorStyleItemDefaults.strings(
						colorStyle = stringResource(id = R.string.set_color_style),
						catNFact = stringResource(id = R.string.cat_n_fact),
						asSystem = stringResource(id = R.string.as_system)
					),
					onChangeColorStyle = {
						onUiSettingsAction(SetColorStyleIntent(it))
					}
				)
			}

			if (uiState.autoTranslate.visible) {
				item {
					AutoTranslateItem(
						state = uiState.autoTranslate.autoTranslate,
						enabled = uiState.autoTranslate.enabled,
						headlineText = stringResource(id = R.string.auto_translate_to_locale),
						onChangeState = {
							onUiSettingsAction(SetAutoTranslate(it))
						}
					)
				}
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
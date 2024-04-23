package kanti.catnfact.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import kanti.catnfact.feat.settings.R
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun DarkModeItem(
	modifier: Modifier = Modifier,
	state: DarkModeUiState,
	strings: DarkModeStrings = DarkModeItemDefaults.strings(),
	onChangeDarkMode: (DarkModeUiState) -> Unit = {}
) = Box(
	modifier = modifier
) {
	var showMenu by rememberSaveable { mutableStateOf(false) }
	fun onDismissShowMenu() { showMenu = false }

	ListItem(
		modifier = Modifier
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = rememberRipple(),
				role = Role.Button,
				onClick = { showMenu = !showMenu }
			),
		headlineContent = {
			Text(text = strings.darkMode)
		},
		supportingContent = {
			Text(text = strings.darkMode(state = state))
		},
		leadingContent = {
			Icon(
				painter = painterResource(id = R.drawable.nightlight_round),
				contentDescription = null
			)
		}
	)
	DropdownMenu(expanded = showMenu, onDismissRequest = ::onDismissShowMenu) {
		DropdownMenuItem(
			text = { Text(text = strings.light) },
			onClick = {
				onChangeDarkMode(DarkModeUiState.Light)
				onDismissShowMenu()
			}
		)
		DropdownMenuItem(
			text = { Text(text = strings.dark) },
			onClick = {
				onChangeDarkMode(DarkModeUiState.Dark)
				onDismissShowMenu()
			}
		)
		DropdownMenuItem(
			text = { Text(text = strings.asSystem) },
			onClick = {
				onChangeDarkMode(DarkModeUiState.AsSystem)
				onDismissShowMenu()
			}
		)
	}
}

enum class DarkModeUiState {

	Light, Dark, AsSystem
}

@Immutable
data class DarkModeStrings(
	val darkMode: String,
	val light: String,
	val dark: String,
	val asSystem: String
) {

	fun darkMode(state: DarkModeUiState): String {
		return when (state) {
			DarkModeUiState.Light -> light
			DarkModeUiState.Dark -> dark
			DarkModeUiState.AsSystem -> asSystem
		}
	}
}

object DarkModeItemDefaults {

	fun strings(
		darkMode: String = "Set app dark mode",
		light: String = "Light",
		dark: String = "Dark",
		asSystem: String = "System"
	): DarkModeStrings = DarkModeStrings(
		darkMode = darkMode,
		light = light,
		dark = dark,
		asSystem = asSystem
	)
}

@PreviewLightDark
@Composable
private fun PreviewDarkModeItem(
	@PreviewParameter(DarkModeStates::class) state: DarkModeUiState
) {
	CatNFactTheme {
		DarkModeItem(
			state = state
		)
	}
}

private class DarkModeStates : CollectionPreviewParameterProvider<DarkModeUiState>(
	listOf(
		DarkModeUiState.Light,
		DarkModeUiState.Dark,
		DarkModeUiState.AsSystem
	)
)
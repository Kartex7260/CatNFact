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
fun ColorStyleItem(
	modifier: Modifier = Modifier,
	state: ColorStyleUiState,
	strings: ColorStyleStrings = ColorStyleItemDefaults.strings(),
	onChangeColorStyle: (ColorStyleUiState) -> Unit = {}
) = Box(
	modifier = modifier
) {
	var showMenu by rememberSaveable { mutableStateOf(false) }
	fun onDismissShowMenu() {
		showMenu = false
	}

	ListItem(
		modifier = Modifier
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = rememberRipple(),
				role = Role.Button,
				onClick = { showMenu = !showMenu }
			),
		headlineContent = {
			Text(text = strings.colorStyle)
		},
		supportingContent = {
			Text(text = strings.colorStyle(state = state))
		},
		leadingContent = {
			Icon(
				painter = painterResource(id = R.drawable.color_lens),
				contentDescription = null
			)
		}
	)
	DropdownMenu(expanded = showMenu, onDismissRequest = ::onDismissShowMenu) {
		DropdownMenuItem(
			text = { Text(text = strings.catNFact) },
			onClick = {
				onChangeColorStyle(ColorStyleUiState.CatNFact)
				onDismissShowMenu()
			}
		)
		DropdownMenuItem(
			text = { Text(text = strings.asSystem) },
			onClick = {
				onChangeColorStyle(ColorStyleUiState.AsSystem)
				onDismissShowMenu()
			}
		)
	}
}

enum class ColorStyleUiState {

	CatNFact, AsSystem
}

@Immutable
data class ColorStyleStrings(
	val colorStyle: String,
	val catNFact: String,
	val asSystem: String
) {

	fun colorStyle(state: ColorStyleUiState): String {
		return when (state) {
			ColorStyleUiState.CatNFact -> catNFact
			ColorStyleUiState.AsSystem -> asSystem
		}
	}
}

object ColorStyleItemDefaults {

	fun strings(
		colorStyle: String = "Set app color style",
		catNFact: String = "CatNFact",
		asSystem: String = "As system"
	): ColorStyleStrings = ColorStyleStrings(
		colorStyle = colorStyle,
		catNFact = catNFact,
		asSystem = asSystem
	)
}

@PreviewLightDark
@Composable
private fun PreviewColorStyleItem(
	@PreviewParameter(ColorStyleStates::class) state: ColorStyleUiState
) {
	CatNFactTheme {
		ColorStyleItem(
			state = state
		)
	}
}

private class ColorStyleStates : CollectionPreviewParameterProvider<ColorStyleUiState>(
	listOf(
		ColorStyleUiState.CatNFact,
		ColorStyleUiState.AsSystem
	)
)
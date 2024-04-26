package kanti.catnfact.ui.components.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import kanti.catnfact.feat.settings.R
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun AutoTranslateItem(
	modifier: Modifier = Modifier,
	headlineText: String = "Automatic translation to the device locale",
	state: Boolean,
	onChangeState: (Boolean) -> Unit = {}
) = ListItem(
	modifier = modifier,
	headlineContent = { Text(text = headlineText) },
	leadingContent = { Icon(painter = painterResource(id = R.drawable.baseline_translate_24), contentDescription = null) },
	trailingContent = { Switch(checked = state, onCheckedChange = onChangeState) }
)

@PreviewLightDark
@Composable
private fun PreviewAutoTranslateItem(
	@PreviewParameter(State::class) state: Boolean
) {
	CatNFactTheme {
		AutoTranslateItem(state = state)
	}
}

private class State : CollectionPreviewParameterProvider<Boolean>(listOf(false, true))
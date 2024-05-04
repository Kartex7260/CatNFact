package kanti.catnfact.ui.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
	enabled: Boolean = true,
	onChangeState: (Boolean) -> Unit = {}
) {
	val textColor = if (enabled) MaterialTheme.colorScheme.onSurface
	else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

	val iconColor = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
	else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

	ListItem(
		modifier = modifier,
		headlineContent = { Text(text = headlineText, color = textColor) },
		leadingContent = {
			Icon(
				painter = painterResource(id = R.drawable.baseline_translate_24),
				contentDescription = null,
				tint = iconColor
			)
		},
		trailingContent = {
			Switch(checked = state, onCheckedChange = onChangeState, enabled = enabled)
		}
	)
}

@PreviewLightDark
@Composable
private fun PreviewAutoTranslateItem(
	@PreviewParameter(State::class) state: Boolean
) {
	CatNFactTheme {
		Column {
			AutoTranslateItem(state = state, enabled = true)
			AutoTranslateItem(state = state, enabled = false)
		}
	}
}

private class State : CollectionPreviewParameterProvider<Boolean>(listOf(false, true))
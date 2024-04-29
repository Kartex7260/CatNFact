package kanti.catnfact.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun ExpandedCard(
	modifier: Modifier = Modifier,
	expanded: Boolean,
	allowExpanding: Boolean = true,
	onChangeExpand: (Boolean) -> Unit = {},
	content: @Composable ColumnScope.() -> Unit
) {
	val containerColorAnimate by animateColorAsState(
		targetValue = with(MaterialTheme.colorScheme) {
			if (expanded) surfaceContainerLow else surfaceContainerHighest
		},
		label = "containerColor"
	)
	val elevationAnimate by animateDpAsState(
		targetValue = if (expanded) 1.dp else 0.dp,
		label = "elevation"
	)

	Card(
		modifier = Modifier
			.then(modifier)
			.shadow(
				elevation = elevationAnimate,
				shape = CardDefaults.shape
			)
			.clip(CardDefaults.shape)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = rememberRipple(),
				role = Role.Switch,
				enabled = allowExpanding,
				onClick = { onChangeExpand(!expanded) }
			),
		colors = CardDefaults.cardColors(containerColor = containerColorAnimate),
		content = content
	)
}

@PreviewLightDark
@Composable
private fun PreviewFactCard() {
	var isExpand by remember { mutableStateOf(false) }
	CatNFactTheme {
		Surface {
			ExpandedCard(
				modifier = Modifier.padding(20.dp),
				expanded = isExpand,
				onChangeExpand = { isExpand = it }
			) {
				Text(text = "Test")
			}
		}
	}
}
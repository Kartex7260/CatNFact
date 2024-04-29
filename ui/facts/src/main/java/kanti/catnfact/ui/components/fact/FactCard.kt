package kanti.catnfact.ui.components.fact

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.components.ExpandedCard
import kanti.catnfact.ui.components.FavouriteButton
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FactCard(
	modifier: Modifier = Modifier,
	state: FactUiState,
	onChangeExpand: (Boolean) -> Unit = {},
	onChangeFavourite: () -> Unit = {}
) {
	var enabledClick by rememberSaveable { mutableStateOf(false) }
	ExpandedCard(
		modifier = modifier,
		expanded = state.isExpand,
		allowExpanding = enabledClick,
		onChangeExpand = onChangeExpand
	) {
		Row(
			modifier = Modifier
				.padding(
					top = 12.dp,
					bottom = 12.dp,
					start = 16.dp,
					end = 4.dp
				)
		) {
			Text(
				modifier = Modifier
					.weight(1f)
					.animateContentSize()
					.align(Alignment.CenterVertically),
				text = state.fact,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = if (state.isExpand) Int.MAX_VALUE else 2,
				overflow = TextOverflow.Ellipsis,
				onTextLayout = { layoutResult ->
					enabledClick = layoutResult.hasVisualOverflow || layoutResult.lineCount > 2
				}
			)

			FavouriteButton(
				isFavourite = state.isFavourite,
				onChangeFavourite = onChangeFavourite
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewFactCard(
	@PreviewParameter(Facts::class) fact: String
) {
	var isExpand by remember { mutableStateOf(false) }
	CatNFactTheme {
		FactCard(
			state = FactUiState(
				fact = fact,
				isFavourite = false,
				isExpand = isExpand
			),
			onChangeExpand = { isExpand = it }
		)
	}
}

class Facts : CollectionPreviewParameterProvider<String>(
	listOf(
		"Small fact",
		"The cat who holds the record for the longest non-fatal fall is Andy. " +
				"He fell from the 16th floor of an apartment building " +
				"(about 200 ft/.06 km) and survived."
	)
)
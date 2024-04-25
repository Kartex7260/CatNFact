package kanti.catnfact.ui.components.fact

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FactCard(
	modifier: Modifier = Modifier,
	state: FactUiState,
	onChangeExpand: (Boolean) -> Unit = {},
	onChangeFavourite: () -> Unit = {}
) {
	val containerColorAnimate by animateColorAsState(
		targetValue = with(MaterialTheme.colorScheme) {
			if (state.isExpand) surfaceContainerLow else surfaceContainerHighest
		},
		label = "containerColor"
	)
	val elevationAnimate by animateDpAsState(
		targetValue = if (state.isExpand) 1.dp else 0.dp,
		label = "elevation"
	)

	Card(
		modifier = Modifier
			.then(modifier),
		colors = CardDefaults.cardColors(containerColor = containerColorAnimate),
		elevation = CardDefaults.cardElevation(defaultElevation = elevationAnimate),
		onClick = { onChangeExpand(!state.isExpand) }
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
			Box(
				modifier = Modifier
					.weight(1f)
					.animateContentSize()
			) {
				androidx.compose.animation.AnimatedVisibility(
					visible = state.isExpand,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Text(
						text = state.fact,
						style = MaterialTheme.typography.bodyLarge
					)
				}
				androidx.compose.animation.AnimatedVisibility(
					visible = !state.isExpand,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Text(
						text = state.fact,
						style = MaterialTheme.typography.bodyLarge,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)
				}
			}


			IconButton(
				onClick = onChangeFavourite
			) {
				Icon(
					painter = painterResource(
						id = if (state.isFavourite) R.drawable.round_star_24
						else R.drawable.round_star_outline_24
					),
					contentDescription = null
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun PreviewFactCard(
	@PreviewParameter(IsFavourite::class) isFavourite: Boolean
) {
	CatNFactTheme {
		FactCard(
			state = FactUiState(
				fact = "The cat who holds the record for the longest non-fatal fall is Andy. " +
						"He fell from the 16th floor of an apartment building " +
						"(about 200 ft/.06 km) and survived.",
				isFavourite = isFavourite
			)
		)
	}
}

class IsFavourite : CollectionPreviewParameterProvider<Boolean>(listOf(false, true))
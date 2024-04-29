package kanti.catnfact.ui.components.breed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.components.ExpandedCard
import kanti.catnfact.ui.components.FavouriteButton
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun BreedCard(
	modifier: Modifier = Modifier,
	state: BreedUiState,
	strings: BreedStrings = BreedCardDefault.strings(),
	contentPadding: PaddingValues = BreedCardDefault.contentPadding,
	onChangeExpand: (Boolean) -> Unit = {},
	onChangeFavourite: () -> Unit = {}
) {
	ExpandedCard(
		modifier = modifier,
		expanded = state.isExpand,
		onChangeExpand = onChangeExpand
	) {
		fun maxLineFromExpand() = if (state.isExpand) Int.MAX_VALUE else 1

		Row(
			modifier = Modifier.padding(contentPadding),
			verticalAlignment = Alignment.Top
		) {
			Column(
				modifier = Modifier
					.weight(1f),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = state.breed,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = maxLineFromExpand(),
					overflow = TextOverflow.Ellipsis
				)

				AnimatedVisibility(
					visible = state.isExpand,
					enter = expandVertically() + fadeIn(),
					exit = shrinkVertically() + fadeOut()
				) {
					Column {
						Spacer(modifier = Modifier.height(4.dp))
						Text(
							text = "${strings.origin}: ${state.origin}",
							style = MaterialTheme.typography.bodyMedium,
							maxLines = maxLineFromExpand(),
							overflow = TextOverflow.Ellipsis
						)
						Spacer(modifier = Modifier.height(4.dp))
						Text(
							text = "${strings.coat}: ${state.coat}",
							style = MaterialTheme.typography.bodyMedium,
							maxLines = maxLineFromExpand(),
							overflow = TextOverflow.Ellipsis
						)
						Spacer(modifier = Modifier.height(4.dp))
						Text(
							text = "${strings.pattern}: ${state.pattern}",
							style = MaterialTheme.typography.bodyMedium,
							maxLines = maxLineFromExpand(),
							overflow = TextOverflow.Ellipsis
						)
						Spacer(modifier = Modifier.height(4.dp))
					}
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Box(
						modifier = Modifier
							.weight(1f, false)
					) {
						androidx.compose.animation.AnimatedVisibility(
							visible = state.isExpand,
							enter = fadeIn() + expandHorizontally(),
							exit = fadeOut() + shrinkHorizontally()
						) {
							Text(
								text = "${strings.country}: ${state.country}",
								color = MaterialTheme.colorScheme.onSurfaceVariant,
								style = MaterialTheme.typography.bodySmall
							)
						}
						androidx.compose.animation.AnimatedVisibility(
							visible = !state.isExpand,
							enter = fadeIn(),
							exit = fadeOut()
						) {
							Text(
								text = state.country,
								color = MaterialTheme.colorScheme.onSurfaceVariant,
								style = MaterialTheme.typography.bodySmall,
								maxLines = 1,
								overflow = TextOverflow.Ellipsis
							)
						}
					}

					Spacer(modifier = Modifier.width(4.dp))
					val rotateAnimate by animateFloatAsState(
						targetValue = if (state.isExpand) 180f else 0f,
						animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
						label = "rotate"
					)
					Icon(
						modifier = Modifier
							.rotate(rotateAnimate),
						imageVector = Icons.Default.KeyboardArrowDown,
						contentDescription = null
					)
				}
			}

			FavouriteButton(
				isFavourite = state.isFavourite,
				onChangeFavourite = onChangeFavourite
			)
		}
	}
}

@Immutable
data class BreedStrings(
	val origin: String,
	val coat: String,
	val pattern: String,
	val country: String
)

object BreedCardDefault {

	val contentPadding: PaddingValues = PaddingValues(
		top = 12.dp,
		bottom = 12.dp,
		start = 16.dp,
		end = 4.dp
	)

	fun strings(
		origin: String = "Origin",
		coat: String = "Coat",
		pattern: String = "Pattern",
		country: String = "Country"
	): BreedStrings = BreedStrings(
		origin = origin,
		coat = coat,
		pattern = pattern,
		country = country
	)
}

@PreviewLightDark
@Composable
private fun PreviewBreedCard(
	@PreviewParameter(Breeds::class) breed: BreedUiState
) {
	CatNFactTheme {
		Surface {
			var isExpand by remember { mutableStateOf(false) }
			Box(modifier = Modifier.padding(16.dp)) {
				BreedCard(
					state = breed.copy(
						isExpand = isExpand
					),
					onChangeExpand = { isExpand = it }
				)
			}
		}
	}
}

class Breeds : CollectionPreviewParameterProvider<BreedUiState>(
	listOf(
		BreedUiState(
			hash = "1",

			breed = "Asian Semi-longhair",
			country = "United Kingdom",
			origin = "Crossbreed",
			coat = "Semi-long",
			pattern = "Solid",

			isExpand = false,
			isFavourite = true
		),
		BreedUiState(
			hash = "2",

			breed = "Balinese",
			country = "developed in the United States (founding stock from Thailand)",
			origin = "Crossbreed",
			coat = "Long",
			pattern = "Colorpoint"
		)
	)
)
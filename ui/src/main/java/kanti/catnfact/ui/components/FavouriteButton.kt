package kanti.catnfact.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.R
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun FavouriteButton(
	modifier: Modifier = Modifier,
	isFavourite: Boolean,
	onChangeFavourite: () -> Unit = {}
) {
	IconButton(
		modifier = modifier,
		onClick = onChangeFavourite
	) {
		Icon(
			painter = painterResource(
				id = if (isFavourite) R.drawable.round_star_24
				else R.drawable.round_star_outline_24
			),
			contentDescription = null
		)
	}
}

@PreviewLightDark
@Composable
private fun PreviewFavouriteButton() {
	CatNFactTheme {
		Surface {
			Column(
				modifier = Modifier.padding(16.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				FavouriteButton(isFavourite = true)
				FavouriteButton(isFavourite = false)
			}
		}
	}
}
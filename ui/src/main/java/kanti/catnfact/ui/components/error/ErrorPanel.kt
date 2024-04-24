package kanti.catnfact.ui.components.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kanti.catnfact.ui.theme.CatNFactTheme

@Composable
fun ErrorPanel(
	modifier: Modifier = Modifier,
	state: ErrorState,
	onCallback: () -> Unit = {}
) = Box(
	modifier = Modifier
		.background(color = MaterialTheme.colorScheme.errorContainer)
		.padding(horizontal = 16.dp, vertical = 12.dp)
		.then(modifier),
) {
	Row(
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Column(
			modifier = Modifier
				.align(Alignment.Top)
				.weight(1f)
		) {
			Text(
				text = state.title,
				style = MaterialTheme.typography.titleMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				color = MaterialTheme.colorScheme.onErrorContainer
			)
			if (state.support != null) {
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = state.support,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.error
				)
			}
		}

		Spacer(modifier = Modifier.width(16.dp))

		TextButton(
			modifier = Modifier.align(Alignment.Bottom),
			colors = ButtonDefaults.textButtonColors(
				containerColor = MaterialTheme.colorScheme.errorContainer,
				contentColor = MaterialTheme.colorScheme.onErrorContainer
			),
			onClick = onCallback
		) {
			Text(text = state.callbackLabel)
		}
	}
}

@Immutable
data class ErrorState(
	val title: String,
	val support: String? = null,
	val callbackLabel: String
)

@PreviewLightDark
@Composable
private fun PreviewErrorPanel() {
	CatNFactTheme {
		ErrorPanel(
			state = ErrorState(
				title = "Test test test test test test test test test test test test test test test test test",
				support = "Test support test support test support test support test support test support test support",
				callbackLabel = "Callback"
			)
		)
	}
}
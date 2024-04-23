package kanti.catnfact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kanti.catnfact.ui.theme.CatNFactTheme
import kanti.catnfact.ui.theme.ColorStyle

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MainContent()
		}
	}
}

@Composable
fun MainContent() {
	CatNFactTheme(
		colorStyle = ColorStyle.AsSystem
	) {
		Surface(
			modifier = Modifier.fillMaxSize()
		) {
		}
	}
}
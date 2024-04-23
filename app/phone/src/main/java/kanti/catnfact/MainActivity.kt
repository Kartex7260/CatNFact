package kanti.catnfact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	private val viewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			val darkMode by viewModel.darkMode.collectAsState()
			val colorStyle by viewModel.colorStyle.collectAsState()

			MainContent(
				darkMode = darkMode,
				colorStyle = colorStyle
			)
		}
	}
}

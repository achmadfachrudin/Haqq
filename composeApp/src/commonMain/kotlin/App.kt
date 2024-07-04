import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import core.ui.theme.HaqqTheme
import feature.home.screen.MainScreen

@Composable
fun App() {
    HaqqTheme {
        Navigator(
            screen = MainScreen(),
        )
    }
}

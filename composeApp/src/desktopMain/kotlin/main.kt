import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import twine.presentation.ui.ProfileScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PrancingPony",
    ) {
        ProfileScreen(BatterySettings())
    }
}
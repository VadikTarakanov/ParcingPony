package twine.presentation.ui

import androidx.compose.runtime.Composable
import twine.data.model.SplitProgressModel

actual class CameraScreen {

    @Composable actual fun CameraPreview(
        cameraLens: Int,
        onResult: (SplitProgressModel, Boolean) -> Unit
    ) {
    }
}
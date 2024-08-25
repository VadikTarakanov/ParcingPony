package twine.presentation.ui

import androidx.compose.runtime.Composable

expect class CameraScreen {

    @Composable
    fun CameraPreview(onLensChange: () -> Unit, cameraLens: Int)
}
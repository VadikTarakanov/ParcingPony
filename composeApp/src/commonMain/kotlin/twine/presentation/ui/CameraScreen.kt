package twine.presentation.ui

import androidx.compose.runtime.Composable
import twine.data.model.SplitProgressModel
import twine.presentation.model.TypeTraining

expect class CameraScreen {

    @Composable
    fun CameraPreview(
        cameraLens: Int,
        onResult: (SplitProgressModel, Boolean, TypeTraining) -> Unit
    )
}
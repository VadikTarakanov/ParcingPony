package twine.presentation.ui

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

actual class CameraScreen(
    private val modifier: Modifier = Modifier,
    private val scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER
) {

    @Composable
    actual fun CameraPreview(onLensChange: () -> Unit, cameraLens: Int) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val previewView = remember { PreviewView(context) }
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)
                .configureCamera(previewView, lifecycleOwner, cameraLens, context)


        AndroidView(
            modifier = modifier,
            factory = {
                previewView.apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
                previewView
            })

        Controls(onLensChange = onLensChange)
    }

    private fun ListenableFuture<ProcessCameraProvider>.configureCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraLens: Int,
        context: Context
    ): ListenableFuture<ProcessCameraProvider> {
        addListener({
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }
            try {
                get().apply {
                    unbindAll()
                    bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.Builder().requireLensFacing(cameraLens).build(),
                        preview
                    )
                }
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
        return this
    }

    @Composable
    fun Controls(
        onLensChange: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Button(
                onClick = onLensChange,
                modifier = Modifier.wrapContentSize()
            ) {
                Icon(Icons.Filled.Face, contentDescription = "Switch camera")
            }
        }
    }
}
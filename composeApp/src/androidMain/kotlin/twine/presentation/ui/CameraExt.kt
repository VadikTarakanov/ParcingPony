package twine.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.tasks.TaskExecutors
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.MlKitException
import twine.presentation.data.Person
import twine.presentation.detection.PoseDetector
import twine.presentation.model.SourceInfo
import twine.presentation.model.TypeTraining
import twine.presentation.utils.createBitmapForMl

fun ListenableFuture<ProcessCameraProvider>.configureCamera(
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
    cameraLens: Int,
    context: Context,
    setSourceInfo: (SourceInfo) -> Unit,
    onPoseDetected: (List<Person>) -> Unit,
    detector: PoseDetector,
    isTrainingStart: Boolean,
    typeTraining: TypeTraining
): ListenableFuture<ProcessCameraProvider> {
    addListener({
        val cameraSelector = CameraSelector.Builder().requireLensFacing(cameraLens).build()

        val preview = androidx.camera.core.Preview.Builder()
            .build()
            .apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }
        val analysis =
            bindAnalysisUseCase(
                lens = cameraLens,
                setSourceInfo = setSourceInfo,
                onPoseDetected = onPoseDetected,
                detector = detector,
                isTrainingStart = isTrainingStart,
                typeTraining = typeTraining
            )
        try {
            get().apply {
                unbindAll()
                bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                if (isTrainingStart) {
                    bindToLifecycle(lifecycleOwner, cameraSelector, analysis)
                }
            }
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(context))
    return this
}

@SuppressLint("RestrictedApi")
fun bindAnalysisUseCase(
    lens: Int,
    setSourceInfo: (SourceInfo) -> Unit,
    onPoseDetected: (List<Person>) -> Unit,
    detector: PoseDetector,
    isTrainingStart: Boolean,
    typeTraining: TypeTraining
): ImageAnalysis {

    val builder = ImageAnalysis.Builder()

    val analysisUseCase = builder.build()

    var sourceInfoUpdated = false

    analysisUseCase.setAnalyzer(
        TaskExecutors.MAIN_THREAD
    ) { imageProxy: ImageProxy ->

        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        if (!sourceInfoUpdated) {
            setSourceInfo(obtainSourceInfo(lens, imageProxy))
            sourceInfoUpdated = true
        }

        val matrixDegrees = when (rotationDegrees) {
            CameraConstant.ROTATION_0 -> CameraConstant.rotationMatrix0
            CameraConstant.ROTATION_90 -> CameraConstant.rotationMatrix90
            CameraConstant.ROTATION_180 -> CameraConstant.rotationMatrix180
            CameraConstant.ROTATION_270 -> CameraConstant.rotationMatrix270
            else -> CameraConstant.rotationMatrix0
        }

        try {
            Log.d("my_tag", "isTrainingStart ${isTrainingStart}")
            val persons =
                if (isTrainingStart) {
                    detector.estimatePoses(
                        bitmap = imageProxy.createBitmapForMl(matrixDegrees),
                        typeTraining = typeTraining
                    )
                } else {
                    detector.close()
                    null
                }

            if (persons != null) {
                onPoseDetected.invoke(persons)
            }

            imageProxy.close()
        } catch (e: MlKitException) {
            Log.e("CAMERA", "Failed to process image. Error: " + e.localizedMessage)
        }
    }
    return analysisUseCase
}

fun obtainSourceInfo(lens: Int, imageProxy: ImageProxy): SourceInfo {
//    Log.d("my_tag2", "obtainSourceInfo rotationDegrees : ${imageProxy.imageInfo.rotationDegrees}")
//    Log.d("my_tag2", "obtainSourceInfo image height : ${imageProxy.height}, width ${imageProxy.width}")
    val isImageFlipped = lens == CameraSelector.LENS_FACING_FRONT
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    return if (rotationDegrees == 0 || rotationDegrees == 180) {
        //       Log.d("my_tag2", "obtainSourceInfo create 1 varinat")
        SourceInfo(
            height = imageProxy.height, width = imageProxy.width, isImageFlipped = isImageFlipped
        )
    } else {
        //    Log.d("my_tag2", "obtainSourceInfo create 2 varinat")
        SourceInfo(
            height = imageProxy.width, width = imageProxy.height, isImageFlipped = isImageFlipped
        )
    }
}
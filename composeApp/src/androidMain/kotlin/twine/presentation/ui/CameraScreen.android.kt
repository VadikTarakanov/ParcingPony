package twine.presentation.ui

import android.util.Log
import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import twine.data.model.SplitProgressModel
import twine.presentation.data.BodyPart
import twine.presentation.data.Device
import twine.presentation.data.Person
import twine.presentation.detection.PoseDetectorImpl
import twine.presentation.model.PreviewScaleType
import twine.presentation.model.SourceInfo
import twine.presentation.model.TypeTraining
import twine.presentation.utils.VisualizationUtils.bodyJoints

actual class CameraScreen(
    private val modifier: Modifier = Modifier,
    private val isTrainingStart: MutableState<Boolean>,
    private val typeTraining: TypeTraining
) {
    private var detector: PoseDetectorImpl? = null

    @Composable
    actual fun CameraPreview(
        cameraLens: Int,
        onResult: (SplitProgressModel, Boolean, TypeTraining) -> Unit
    ) {
        Log.d("my_tag", "CameraPreview {onCameraPreview Call ${typeTraining}}")
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val orientation = LocalConfiguration.current.orientation

        val previewView = remember { PreviewView(context) }
        var sourceInfo by remember { mutableStateOf(SourceInfo(10, 10, false)) }
        var detectedPose by remember { mutableStateOf<List<Person>?>(null) }

        val isTrainingStartLocal = remember(isTrainingStart.value) {
            isTrainingStart.value
        }

        detector = remember(sourceInfo, cameraLens, orientation, isTrainingStart.value) {
            PoseDetectorImpl.create(context, Device.CPU)
        }

        val height = sourceInfo.height

        val width = sourceInfo.width

        remember(sourceInfo, cameraLens, orientation, isTrainingStartLocal, typeTraining) {
            ProcessCameraProvider.getInstance(context)
                .configureCamera(
                    previewView, lifecycleOwner, cameraLens, context,
                    setSourceInfo = { sourceInfo = it },
                    onPoseDetected = {
                        detectedPose = it
                        onResult.invoke(
                            SplitProgressModel(
                                rightAngle = it.firstOrNull()?.rightAngle,
                                leftAngle = it.firstOrNull()?.leftAngle,
                                time = 40.0
                            ),
                            isTrainingStartLocal,
                            typeTraining
                        )
                    },
                    detector = detector ?: throw Exception("Camera Preview: Pose Detector can't be null"),
                    isTrainingStart = isTrainingStartLocal,
                    typeTraining = typeTraining
                )
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            with(LocalDensity.current) {
                Box(
                    modifier = Modifier
                        .size(
                            height = height.toDp(),
                            width = width.toDp()
                        )
                        .scale(
                            calculateScale(
                                constraints,
                                height = height,
                                width = width,
                                scaleType = PreviewScaleType.CENTER_CROP
                            )
                        )
                )
                {
                    AndroidCamera(previewView = previewView, modifier = modifier)
                    DetectedPose(
                        persons = detectedPose,
                        sourceInfo = sourceInfo,
                        isVisible = isTrainingStartLocal
                    )
                }
            }
        }
    }

    @Composable
    fun AndroidCamera(
        modifier: Modifier = Modifier,
        previewView: PreviewView
    ) {
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
    }

    @Composable
    fun DetectedPose(persons: List<Person>?, sourceInfo: SourceInfo, isVisible: Boolean) {
        Log.d("my_tag4", "persons ${persons?.firstOrNull()?.score}")
//      Log.d("my_tag4", "persons left angle ${persons.firstOrNull()?.leftAngle}")
//      Log.d("my_tag4", "persons right angle ${persons.firstOrNull()?.rightAngle?.toInt()}")

        val person = persons?.firstOrNull { it.score >= 0.50 }
        if (person == null) {
            if (isVisible) {
                ObjectNotDetected()
            }
            return
        }
        val isNeedToMirror = sourceInfo.isImageFlipped
        if (isVisible) {
            DrawBody(
                modifier = Modifier.fillMaxSize(),
                persons = persons,
                isNeedToMirror = isNeedToMirror
            )
        }
    }

    @Composable
    fun DrawBody(
        modifier: Modifier = Modifier,
        persons: List<Person>,
        isNeedToMirror: Boolean
    ) {
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = modifier) {

            val brush = Brush.verticalGradient(listOf(Color.Yellow, Color.Red))
            persons.forEach { person ->
                bodyJoints.forEach { bodyJ ->
                    val pointA = person.keyPoints[bodyJ.first.position].coordinate
                    val pointB = person.keyPoints[bodyJ.second.position].coordinate
                    val startX =
                        if (isNeedToMirror) size.width - pointA.x else pointA.x
                    val startY = pointA.y
                    val endX =
                        if (isNeedToMirror) size.width - pointB.x else pointB.x
                    val endY = pointB.y

                    drawLine(
                        brush = brush,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                person.keyPoints.forEach { point ->
                    drawCircle(
                        brush = brush,
                        center = Offset(
                            if (isNeedToMirror) size.width - point.coordinate.x else point.coordinate.x,
                            point.coordinate.y
                        ),
                        radius = 4f
                    )

                    if (point.bodyPart.position == BodyPart.RIGHT_SHOULDER.position) {
                        drawText(
                            text = person.rightPartBody?.toInt().toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            ),
                            textMeasurer = textMeasurer,
                            topLeft = Offset(
                                if (isNeedToMirror) size.width - point.coordinate.x - 2 else point.coordinate.x - 2,
                                point.coordinate.y
                            )
                        )
                    }

                    if (point.bodyPart.position == BodyPart.LEFT_SHOULDER.position) {
                        drawText(
                            text = person.leftPartBody?.toInt().toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            ),
                            textMeasurer = textMeasurer,
                            topLeft = Offset(
                                if (isNeedToMirror) size.width - point.coordinate.x - 2 else point.coordinate.x - 2,
                                point.coordinate.y
                            )
                        )
                    }

                    if (point.bodyPart.position == BodyPart.RIGHT_HIP.position) {
                        drawText(
                            text = person.rightAngleLongitudinal?.toInt().toString(),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            ),
                            textMeasurer = textMeasurer,
                            topLeft = Offset(
                                if (isNeedToMirror) size.width - point.coordinate.x - 2 else point.coordinate.x - 2,
                                point.coordinate.y
                            )
                        )
                    }

                    if (point.bodyPart.position == BodyPart.LEFT_HIP.position) {
//                        drawText(
//                            text = person.leftAngleLongitudinal?.toInt().toString(),
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 4.sp
//                            ),
//                            textMeasurer = textMeasurer,
//                            topLeft = Offset(
//                                if (isNeedToMirror) size.width - point.coordinate.x - 2 else point.coordinate.x - 2,
//                                point.coordinate.y
//                            )
//                        )
                    }

                }
            }
        }
    }

    @Composable
    fun ObjectNotDetected() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Objet not found please change your position",
                fontSize = 10.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    private fun calculateScale(
        constraints: Constraints,
        height: Int,
        width: Int,
        scaleType: PreviewScaleType
    ): Float {
        val heightRatio = constraints.maxHeight.toFloat() / height
        val widthRatio = constraints.maxWidth.toFloat() / width
        return when (scaleType) {
            PreviewScaleType.FIT_CENTER -> kotlin.math.min(heightRatio, widthRatio)
            PreviewScaleType.CENTER_CROP -> kotlin.math.max(heightRatio, widthRatio)
        }
    }
}
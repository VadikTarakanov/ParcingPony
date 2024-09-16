package twine.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_loop_24dp
import prancingpony.composeapp.generated.resources.ic_pause_24dp
import prancingpony.composeapp.generated.resources.ic_play
import twine.di.CommonDependency
import twine.presentation.components.training.TrainingComponent
import twine.utils.SystemTime
import ui.AccentColor
import ui.AccentColorBreigth
import ui.BackgroundColor
import ui.BlueViolet1
import ui.BlueViolet2
import ui.BlueViolet3
import ui.DarkerButtonBlue
import ui.DeepBlue
import ui.LightRed
import ui.PrimaryLightColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val PI: Double = 3.141592653589793

@Composable
fun TrainingScreen(component: TrainingComponent, cameraScreen: CameraScreen, commonDependency: CommonDependency) {

    val result by component.resultInPercent.collectAsState().asFloatState()

    val localStateRotation by remember(key1 = commonDependency.orientationState.value) {
        println("TrainingScreen stateRotation.value ${commonDependency.orientationState.value}")
        commonDependency.orientationState.asIntState()
    }

    println("TrainingScreen localStateRotation modified ${localStateRotation}")

    val statePermission by component.statePermission.collectAsState()

    val isTrainingStart by remember(commonDependency.isTrainingStart.value) {
        commonDependency.isTrainingStart
    }

    if (!isTrainingStart) {
        component.storeSplitResult()
    }

    val openAlertDialog = remember { mutableStateOf(false) }

    println("TrainingScreen statePermission $statePermission")

    if (openAlertDialog.value) {
        AlertDialogPermissionDenied(
            onConfirmation = {
                component.openAppSettings()
                openAlertDialog.value = false
            },
            onDismissRequest = {
                component.requestPermissionCamera()
                openAlertDialog.value = false
            },
            dialogTitle = "Need to give us permission !",
            dialogText = "You can't use application without permission! Please open settings and give permission"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (statePermission) {
            PermissionState.Granted -> {
                println("TrainingScreen CameraPermission Granted :: $statePermission")
                Box(modifier = Modifier.fillMaxSize()) {

                    var lens by remember { mutableStateOf(1) }
                    val duration: Duration = (100L * 1000L).toDuration(DurationUnit.SECONDS)
                    println("duration " + duration.inWholeSeconds)
                    cameraScreen.CameraPreview(
                        cameraLens = lens,
                        onResult = { model, isTrainingStart ->
                            component.saveSplitResult(
                                model = model,
                                isEndTraining = !isTrainingStart
                            )
                        }
                    )
                    Timer(
                        totalTime = 10L * 1000L,
                        handleColor = AccentColorBreigth,
                        inactiveColor = Color.DarkGray,
                        activeColor = AccentColor,
                        modifier = Modifier.size(200.dp),
                        isStartTranig = commonDependency.isTrainingStart
                    )

                    if (result > 0) {
                        ShowResult(modifier = Modifier.fillMaxSize(), result)
                    }

                    Controls(onLensChange = { lens = switchLens(lens) })
                }

            }
            PermissionState.DeniedAlways -> {
                println("TrainingScreen PermissionState.DeniedAlways:: $statePermission")
                openAlertDialog.value = true
            }
            else -> {
                println("TrainingScreen requestPermissionCamera:: $statePermission")
                component.requestPermissionCamera()
            }
        }
    }
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
            modifier = Modifier
                .size(60.dp, 60.dp),
            colors = ButtonDefaults.buttonColors(
                //  backgroundColor = if (!isTimerRunning || currentTime <= 0L) Color.Green else Color.Red
                PrimaryLightColor
            )
        ) {
            Icon(
                Icons.Filled.Face,
                contentDescription = "Switch camera",
                tint = BackgroundColor
            )
        }
    }
}

private fun switchLens(lens: Int) = if (0 == lens) {
    1
} else {
    0
}

@Composable
fun AlertDialogPermissionDenied(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        modifier = Modifier.border(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                2.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        DeepBlue,
                        DarkerButtonBlue,
                        BlueViolet1,
                        BlueViolet2,
                        BlueViolet3,
                        Color.Blue
                    )
                )
            )
        ),
        shape = RoundedCornerShape(corner = CornerSize(20.dp)),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Settings")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    inactiveColor: Color,
    activeColor: Color,
    isStartTranig: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { isStartTranig }
    var lastUpdateTime by remember { mutableStateOf(SystemTime.getCurrentTime()) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            val currentTimeMillis = SystemTime.getCurrentTime()
            val elapsed = currentTimeMillis - lastUpdateTime

            if (currentTime > 0) {
                currentTime -= elapsed
                value = currentTime / totalTime.toFloat()
                lastUpdateTime = currentTimeMillis
            } else {
                isStartTranig.value = false
                isTimerRunning = false
            }

            delay(100L)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged { size = it }.padding(12.dp)
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = BackgroundColor,
            modifier = Modifier.padding(start = 12.dp)
        )

        Button(
            onClick = {
                if (currentTime <= 0L) {
                    currentTime = totalTime
                    lastUpdateTime = SystemTime.getCurrentTime()
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                    if (isTimerRunning) {
                        lastUpdateTime = SystemTime.getCurrentTime()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                //  backgroundColor = if (!isTimerRunning || currentTime <= 0L) Color.Green else Color.Red
                AccentColorBreigth
            ),
            modifier = Modifier.size(50.dp)
                .align(Alignment.BottomCenter)
                .padding(start = 12.dp)
        ) {
            Icon(

                painter = painterResource(
                    resource = when {
                        isTimerRunning && currentTime >= 0L -> Res.drawable.ic_pause_24dp
                        !isTimerRunning && currentTime >= 0L -> Res.drawable.ic_play
                        else -> Res.drawable.ic_loop_24dp
                    }
                ),
                contentDescription = null,
                tint = BackgroundColor
            )

        }
    }
}

@Composable
fun ShowResult(modifier: Modifier = Modifier, result: Float) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "Your result IS ${result}%",
            style = MaterialTheme.typography.h2,
            color = LightRed
        )
    }
}
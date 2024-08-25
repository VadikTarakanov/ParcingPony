package twine.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import twine.presentation.components.training.TrainingComponent
import ui.BlueViolet1
import ui.BlueViolet2
import ui.BlueViolet3
import ui.DarkerButtonBlue
import ui.DeepBlue
import kotlin.math.cos
import kotlin.math.sin

private const val PI: Double = 3.141592653589793

@Composable
fun TrainingScreen(component: TrainingComponent, cameraScreen: CameraScreen) {

    val time by component.timer.collectAsState()

    val statePermission by component.statePermission.collectAsState()

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
                Text("CameraPermission Granted")
                println("TrainingScreen CameraPermission Granted :: $statePermission")
                Box(modifier = Modifier.fillMaxSize()) {

                    var lens by remember { mutableStateOf(0) }
                    cameraScreen.CameraPreview(
                        onLensChange = {
                            lens = switchLens(lens)
                        },
                        cameraLens = lens
                    )
                    Timer(
                        totalTime = 100L * 1000L,
                        handleColor = Color.Green,
                        inactiveColor = Color.DarkGray,
                        activeColor = Color(0xFF37B900),
                        modifier = Modifier.size(200.dp)
                    )
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
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var value by remember {
        mutableStateOf(initialValue)
    }

    var currentTime by remember {
        mutableStateOf(totalTime)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning, block = {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    })

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.onSizeChanged {
            size = it
        }
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

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = (currentTime / 1000L).toString(),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )

            Button(
                onClick = {
                    if (currentTime <= 0L) {
                        currentTime = totalTime
                        isTimerRunning = true
                    } else {
                        isTimerRunning = !isTimerRunning
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
            ) {
                Text(
                    text = if (isTimerRunning && currentTime >= 0L) "Stop"
                    else if (!isTimerRunning && currentTime >= 0L) "Start"
                    else "Restart"
                )
            }

            Button(
                onClick = {
                    currentTime = totalTime
                    value = 1f
                    isTimerRunning = false
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red
                )
            ) {
                Text(
                    text = "Restart"
                )
            }
        }
    }
}
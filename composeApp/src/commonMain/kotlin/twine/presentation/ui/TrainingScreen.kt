package twine.presentation.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_switch_camera_24dp
import prancingpony.composeapp.generated.resources.title_training_longitudinal_split_left
import prancingpony.composeapp.generated.resources.title_training_longitudinal_split_right
import prancingpony.composeapp.generated.resources.title_training_slide_split
import prancingpony.composeapp.generated.resources.title_training_static_leg_workout
import twine.di.CommonDependency
import twine.presentation.components.training.TrainingComponent
import twine.presentation.model.ResultTraining
import twine.presentation.model.TypeTraining
import twine.presentation.model.getTrainingTypeById
import twine.utils.SoundPlayer
import twine.utils.SystemTime
import twine.utils.TimeConverter
import ui.AccentColor
import ui.AccentColorBreigth
import ui.BackgroundColor
import ui.BlueViolet1
import ui.BlueViolet2
import ui.BlueViolet3
import ui.DarkerButtonBlue
import ui.DeepBlue
import ui.Gold
import ui.PrimaryColor
import ui.PrimaryLightColor
import ui.TextWhite
import ui.brushTools
import ui.primaryBrush
import kotlin.math.cos
import kotlin.math.sin

private const val PI: Double = 3.141592653589793

@Composable
fun TrainingScreen(component: TrainingComponent, cameraScreen: CameraScreen, commonDependency: CommonDependency) {
    val visible by component.isShowResult.collectAsState()
    val density = LocalDensity.current

    val result by component.resultTraining.collectAsState()

    val stateFinishingTraining = mutableStateOf(false)

    val isTrainingFinished = remember(stateFinishingTraining.value) {
        println("TrainingScreen stateFinishingTraining.value  ${stateFinishingTraining.value}")
        stateFinishingTraining
    }

    val statePermission by component.statePermission.collectAsState()

    val totalTime = component.getTimeTraining()
    val timerDelayTraining = component.getTimerDelay()

    if (isTrainingFinished.value) {
        component.storeSplitResult()
        println("TrainingScreen component.storeSplitResult() ")
    }

    val openAlertDialog = remember { mutableStateOf(false) }

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
                Box(modifier = Modifier.fillMaxSize()) {

                    var lens by remember { mutableStateOf(1) }

                    cameraScreen.CameraPreview(
                        cameraLens = lens,
                        onResult = { model, isTrainingStart, typeTraining ->
                            component.saveSplitResult(
                                model = model,
                                isEndTraining = !isTrainingStart,
                                typeTraining = typeTraining
                            )
                        }
                    )
                    Column {
                        Timer(
                            totalTime = totalTime * 1000L,
                            timeDelayTraining = timerDelayTraining * 1000L,
                            handleColor = AccentColorBreigth,
                            inactiveColor = Color.DarkGray,
                            activeColor = AccentColor,
                            modifier = Modifier.size(200.dp),
                            isStartTraining = commonDependency.isTrainingStart,
                            timeConverter = commonDependency.timeConvertor,
                            soundPlayer = commonDependency.soundPlayer,
                            isTrainingFinished = isTrainingFinished
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = visible,
                        enter =
                        slideInVertically(
                            animationSpec = tween(delayMillis = 4500, easing = LinearEasing)
                        ) {
                            // Slide in from 40 dp from the top.
                            with(density) { -500.dp.roundToPx() }
                        } + fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            animationSpec = tween(delayMillis = 1500, easing = LinearEasing),
                            initialAlpha = 0.1f
                        ),
                        exit = slideOutVertically(
                            animationSpec = tween(delayMillis = 3500, easing = LinearEasing)
                        ) + shrinkVertically(
                            animationSpec = tween(delayMillis = 1000, easing = LinearEasing)
                        ) + fadeOut(
                            animationSpec = tween(delayMillis = 1500, easing = LinearEasing)
                        )
                    ) {
                        result?.let { ShowTrainingResult(modifier = Modifier.fillMaxSize(), it) }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 24.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (!commonDependency.isTrainingStart.value) {
                                ChipsTrainingSection(
                                    typeTraining = commonDependency.typeTraining,
                                    onItemClick = commonDependency.onTrainingClick,
                                    modifier = Modifier.padding(bottom = 12.dp).align(Alignment.CenterHorizontally)
                                )
                            }

                            Controls(
                                onLensChange = { lens = switchLens(lens) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

            }
            PermissionState.DeniedAlways -> {
                openAlertDialog.value = true
            }
            else -> {
                component.requestPermissionCamera()
            }
        }
    }
}

@Composable
fun Controls(
    onLensChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onLensChange,
        modifier = modifier
            .size(60.dp, 60.dp),
        colors = ButtonDefaults.buttonColors(
            //  backgroundColor = if (!isTimerRunning || currentTime <= 0L) Color.Green else Color.Red
            PrimaryLightColor
        ),
    ) {
        Icon(
            painter = painterResource(
                resource = Res.drawable.ic_switch_camera_24dp
            ),
            contentDescription = null,
            tint = BackgroundColor
        )
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
    timeDelayTraining: Long,
    handleColor: Color,
    inactiveColor: Color,
    activeColor: Color,
    isStartTraining: MutableState<Boolean>,
    timeConverter: TimeConverter,
    soundPlayer: SoundPlayer,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp,
    isTrainingFinished: MutableState<Boolean>,
) {

    val localTimeConvertor = remember { timeConverter }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { isStartTraining }
    var lastUpdateTime by remember { mutableStateOf(SystemTime.getCurrentTime()) }

    //Before start
    var beforeStartValue by remember { mutableStateOf(1f) }
    var beforeStartTime by remember { mutableStateOf(timeDelayTraining) }
    var isBeforeStartTimeRunning by remember { mutableStateOf(false) }
    var isBeforeStartTimerFinished by remember { mutableStateOf(false) }
    var beforeStartLastUpdateTime by remember { mutableStateOf(SystemTime.getCurrentTime()) }

    LaunchedEffect(isTimerRunning) {
        println("Training Screen Second PART STARTS $isTimerRunning")
        while (isTimerRunning) {
            val currentTimeMillis = SystemTime.getCurrentTime()
            val elapsed = currentTimeMillis - lastUpdateTime

            if (currentTime > 0) {
                currentTime -= elapsed
                value = currentTime / totalTime.toFloat()
                lastUpdateTime = currentTimeMillis
            } else {
                println("TrainingScreen  LaunchedEffect(isTimerRunning) ${isStartTraining.value} ")
                soundPlayer.startTrainingSound()
                //  isStartTranig.value = false
                isTrainingFinished.value = true
                isTimerRunning = false
            }
            delay(100L)
        }
    }

    LaunchedEffect(isBeforeStartTimeRunning) {
        while (isBeforeStartTimeRunning) {
            val currentTimeMillis = SystemTime.getCurrentTime()
            val elapsed = currentTimeMillis - beforeStartLastUpdateTime

            if (beforeStartTime > 0) {
                beforeStartTime -= elapsed
                beforeStartValue = beforeStartTime / (timeDelayTraining).toFloat()
                beforeStartLastUpdateTime = currentTimeMillis
                soundPlayer.timerBeforeStartSound()
            } else {
                println("TrainingScreen Before Finished ${isStartTraining.value} ")
                //soundPlayer.playSound()
                isBeforeStartTimerFinished = true
                isBeforeStartTimeRunning = false
                ///
                currentTime = totalTime
                lastUpdateTime = SystemTime.getCurrentTime()
                isTimerRunning = true
                // isTimerRunning = true
            }
            delay(100L)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged { size = it }.padding(12.dp)
    ) {
        Canvas(modifier = modifier) {
            println("Training Screen onSizeChanged width ${size.width}, height ${size.height}")
            println("Training Screen before start value ${beforeStartValue}")
            drawArc(
                brush = primaryBrush,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                alpha = 0.2f
            )

            val sweepAngleMainTime = 250f * value

            drawArc(
                brush = primaryBrush,
                startAngle = -215f,
                sweepAngle = if (sweepAngleMainTime <= 0) 0f else sweepAngleMainTime,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val brush = createStripeBrush(
                stripeColor = BackgroundColor,
                stripeWidth = 2.dp,
                stripeToGapRatio = 1.8f
            )

            val widthOfSubArch = size.width.toFloat() * 0.89.toFloat()
            val heightOfSubArch = size.height.toFloat() * 0.89.toFloat()
            val differenceHeight = (size.height - heightOfSubArch) / 2
            val differenceWidth = (size.width - widthOfSubArch) / 2

            val center = Offset(size.width / 2f, size.height / 2f)
            println("drawArc ${240f * beforeStartValue}")
            val sweepAngelBeforeStart = 240f * beforeStartValue
            drawArc(
                brush = brush,
                startAngle = -210f,
                sweepAngle = if ((sweepAngelBeforeStart) <= 0) 0f else sweepAngelBeforeStart,
                useCenter = false,
                size = Size(widthOfSubArch, heightOfSubArch),
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round),
                topLeft = Offset(differenceWidth, differenceHeight),
                alpha = 0.8f
            )

            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                brush = primaryBrush,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = localTimeConvertor.convertMillisecondsToFormatSeconds(currentTime), //(currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = BackgroundColor,
            modifier = Modifier.padding(start = 12.dp)
        )

        Button(
            onClick = {
                when {
                    !isTimerRunning && !isBeforeStartTimeRunning && !isBeforeStartTimerFinished -> {
                        println("condition 1")
                        if (beforeStartTime <= 0L) {
                            beforeStartTime = timeDelayTraining
                            beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            isBeforeStartTimeRunning = true
                        } else {
                            isBeforeStartTimeRunning = !isBeforeStartTimeRunning
                            if (isBeforeStartTimeRunning) {
                                beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            }
                        }
                    }

                    !isTimerRunning && !isBeforeStartTimeRunning && isBeforeStartTimerFinished && !isTrainingFinished.value -> {
                        println("condition 2")
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
                    }

                    isBeforeStartTimeRunning -> {
                        println("condition 3")
                        if (beforeStartTime <= 0L) {
                            beforeStartTime = timeDelayTraining
                            beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            isBeforeStartTimeRunning = true
                        } else {
                            isBeforeStartTimeRunning = !isBeforeStartTimeRunning
                            if (isBeforeStartTimeRunning) {
                                beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            }
                        }
                    }

                    isTimerRunning -> {
                        println("condition 4")
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
                    }
                    else -> {
                        println("condition 5")
                        isBeforeStartTimerFinished = false
                        isTrainingFinished.value = false
                        value = 1f
                        currentTime = totalTime

                        if (beforeStartTime <= 0L) {
                            beforeStartTime = timeDelayTraining
                            beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            isBeforeStartTimeRunning = true
                        } else {
                            isBeforeStartTimeRunning = !isBeforeStartTimeRunning
                            if (isBeforeStartTimeRunning) {
                                beforeStartLastUpdateTime = SystemTime.getCurrentTime()
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                //  backgroundColor = if (!isTimerRunning || currentTime <= 0L) Color.Green else Color.Red
                PrimaryColor
            ),
            modifier = Modifier.size(height = 58.dp, width = 112.dp)
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp)
        ) {
            Text(
                text = when {
                    isTimerRunning && currentTime >= 0L -> "Stop training"
                    !isTimerRunning && currentTime >= 0L -> "Start training"
                    else -> "Restart"
                },
                style = MaterialTheme.typography.subtitle1,
                color = BackgroundColor,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun GradientButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    gradient: Brush,
    textColor: Color = Color.White,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small) // Применяем форму
            .background(gradient) // Устанавливаем градиентный фон
            .clickable(onClick = onClick, enabled = enabled), // Делаем кнопку кликабельной
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp) // Отступы внутри кнопки
        )
    }
}

fun Density.createStripeBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float
): Brush {
    val stripeWidthPx = stripeWidth.toPx()
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}

@Composable
fun ShowTrainingResult(
    modifier: Modifier = Modifier,
    result: ResultTraining,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val color by infiniteTransition.animateColor(
        initialValue = TextWhite,
        targetValue = Gold,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    when (result) {
        is ResultTraining.CurrentResult -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = result.message,
                        style = MaterialTheme.typography.h1,
                        color = TextWhite,
                        fontSize = 38.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp, start = 18.dp, end = 18.dp).fillMaxWidth()
                    )

                    Text(
                        text = "${result.percentResult.toInt()}%",
                        style = MaterialTheme.typography.h1,
                        color = color,
                        fontSize = 54.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
        is ResultTraining.Failed -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Text(
                    text = result.message,
                    style = MaterialTheme.typography.h1,
                    color = Color.Red,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp, start = 18.dp, end = 18.dp).fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ChipsTrainingSection(
    typeTraining: TypeTraining,
    onItemClick: (TypeTraining) -> Unit,
    list: List<String> = listOf(
        stringResource(Res.string.title_training_slide_split),
        stringResource(Res.string.title_training_longitudinal_split_left),
        stringResource(Res.string.title_training_longitudinal_split_right),
        stringResource(Res.string.title_training_static_leg_workout)
    ),
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {

        items(list.size) {
            val currentBrush = when (it) {
                3 -> brushTools[0]
                2 -> brushTools[1]
                1 -> brushTools[2]
                0 -> brushTools[3]
                else -> primaryBrush
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 16.dp, top = 15.dp, bottom = 10.dp)
                    .clickable {
                        onItemClick.invoke(it.getTrainingTypeById())
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = currentBrush,
                        alpha = if (typeTraining.id == it) 1f else 0.3f
                    ).padding(10.dp)
            ) {
                Text(list[it], color = TextWhite)
            }
        }
    }
}

package twine.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_sports_gymnastics_24dp
import prancingpony.composeapp.generated.resources.ic_sports_martial_arts_24dp
import prancingpony.composeapp.generated.resources.ic_trophy_24dp
import prancingpony.composeapp.generated.resources.title_progress
import prancingpony.composeapp.generated.resources.title_stretching
import prancingpony.composeapp.generated.resources.title_training_longitudinal_split
import prancingpony.composeapp.generated.resources.title_training_slide_split
import prancingpony.composeapp.generated.resources.title_training_static_leg_workout
import twine.presentation.model.BottomMenuContent
import twine.presentation.model.Feature
import ui.AccentColor
import ui.AquaBlue
import ui.BackgroundColor
import ui.ButtonBlue
import ui.DeepBlue
import ui.Gradient1
import ui.Gradient2
import ui.Gradient3
import ui.Gradient4
import ui.Gradient5
import ui.Gradient6
import ui.Gradient7
import ui.Gradient8
import ui.PrimaryColor
import ui.PrimaryDarkColor
import ui.PrimaryLightColor
import ui.PrimaryVeryDarkColor
import ui.PrimaryVeryLightColor
import ui.SecondaryColor
import ui.TextWhite
import ui.brushTools

@Composable
fun MainTwineScreen() {
    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        val listOfFeature = listOf(
            Feature(
                stringResource(Res.string.title_training_slide_split),
                iconResource = Res.drawable.ic_sports_gymnastics_24dp,
                lightColor = PrimaryDarkColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryVeryLightColor
            ),
            Feature(
                stringResource(Res.string.title_training_slide_split),
                iconResource = Res.drawable.ic_sports_gymnastics_24dp,
                lightColor = PrimaryLightColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryLightColor
            ),
            Feature(
                stringResource(Res.string.title_training_static_leg_workout),
                iconResource = Res.drawable.ic_sports_martial_arts_24dp,
                lightColor = PrimaryVeryLightColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryDarkColor
            ),
            Feature(
                stringResource(Res.string.title_progress),
                iconResource = Res.drawable.ic_trophy_24dp,
                lightColor = PrimaryVeryLightColor,
                mediumColor = PrimaryLightColor,
                darkColor = PrimaryColor
            )
        )

        var selectedChipIndex by remember {
            mutableStateOf(0)
        }

        var progress by remember(key1 = selectedChipIndex) {
            //TODO get From Repo
            when (selectedChipIndex) {
                0 -> mutableStateOf(0.90f)
                1 -> mutableStateOf(0.30f)
                else -> mutableStateOf(0.50f)
            }
        }

        Column {
            GreetingSection()
            ChipsSection(
                selectedChipIndex = selectedChipIndex,
                onItemClick = {
                    selectedChipIndex = it
                })
            Row {
                InfoProgress(progress = 90, nameTraining = "Split")
                CustomProgressBar(progress1 = 0.25f, progress2 = 0.40f, progress3 = progress)
            }
            FeatureSection(listOfFeature)
        }
    }
}

@Composable
fun InfoProgress(
    modifier: Modifier = Modifier,
    progress: Int,
    nameTraining: String
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 4.dp, top = 8.dp)
            .width(160.dp),
    ) {
        Text(
            text = "Progress $progress%",
            style = MaterialTheme.typography.h3
        )
        Text(
            text = "Continue to work and you will rich your goal",
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun CustomProgressBar(
    progress1: Float,
    progress2: Float,
    progress3: Float,
    animDuration: Int = 1100,
    animDelay: Int = 0
) {
    var animPlayed by remember {
        mutableStateOf(false)
    }

    val curProgress1 = animateFloatAsState(
        targetValue = if (animPlayed) progress1 else 0f,
        animationSpec = tween(durationMillis = animDuration, delayMillis = animDelay)
    )

    val curProgress2 = animateFloatAsState(
        targetValue = if (animPlayed) progress2 else 0f,
        animationSpec = tween(durationMillis = animDuration, delayMillis = animDelay)
    )

    val curProgress3 = animateFloatAsState(
        targetValue = if (animPlayed) progress3 else 0f,
        animationSpec = tween(durationMillis = animDuration, delayMillis = animDelay)
    )

    LaunchedEffect(key1 = true) {
        animPlayed = true
    }

    val circleSizes = listOf(100.dp, 130.dp, 160.dp) // Размеры кругов
    val strokeWidth = 12.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), contentAlignment = Alignment.Center

    ) {
        circleSizes.forEachIndexed { index, size ->
            // Для каждого круга создадим Canvas
            Canvas(modifier = Modifier.size(size)) {
                val progress = when (index) {
                    0 -> curProgress1
                    1 -> curProgress2
                    else -> curProgress3
                }

                // Рассчитаем размер и положение круга
                val diameter = size.toPx() - strokeWidth.toPx()
                val topLeftOffset = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2)

                drawArc(
                    brush = brushTools[index],
                    startAngle = -90f, // Стартуем сверху
                    sweepAngle = 360f, // Прогресс для круга
                    useCenter = false, // Только линия, не заполняем центр
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                    topLeft = topLeftOffset,
                    size = Size(diameter, diameter),
                    alpha = 0.2f
                )
                // Нарисуем круг
                drawArc(
                    brush = brushTools[index],
                    startAngle = -90f, // Стартуем сверху
                    sweepAngle = 360 * progress.value, // Прогресс для круга
                    useCenter = false, // Только линия, не заполняем центр
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                    topLeft = topLeftOffset,
                    size = Size(diameter, diameter)
                )
            }
        }
    }
}

@Composable
fun GreetingSection(
    name: String = "Vadim"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good morning, $name",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = 10.dp),
                color = PrimaryVeryDarkColor
            )

            Text(
                text = "We wish you have good day!",
                style = MaterialTheme.typography.body1,
                color = SecondaryColor
            )
        }
    }
}

@Composable
fun ChipsSection(
    selectedChipIndex: Int,
    onItemClick: (Int) -> Unit,
    list: List<String> = listOf(
        stringResource(Res.string.title_training_slide_split),
        stringResource(Res.string.title_training_longitudinal_split),
        stringResource(Res.string.title_training_static_leg_workout)
    ),
) {
    LazyRow {
        items(list.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 16.dp, top = 15.dp, bottom = 10.dp)
                    .clickable {
                        onItemClick.invoke(it)
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedChipIndex == it) PrimaryDarkColor
                        else PrimaryLightColor
                    ).padding(10.dp)
            ) {
                Text(list[it], color = TextWhite)
            }
        }
    }
}

@Composable
fun FeatureSection(features: List<Feature>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.title_stretching),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            // color = PrimaryVeryDarkColor
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(features.size) {
                FeatureItem(features[it], index = it)
            }
        }
    }
}

@Composable
fun FeatureItem(
    feature: Feature,
    index: Int
) {
    val drawPathAnimation = remember {
        Animatable(0f)
    }

    val pathMeasure = remember {
        PathMeasure()
    }

    LaunchedEffect(key1 = Unit, block = {
        drawPathAnimation.animateTo(
            1f,
            animationSpec = tween(3000),

            )
    })

    BoxWithConstraints(
        modifier = Modifier.padding(7.5.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Gradient8,
                        Gradient7,
                        Gradient6,
                        Gradient5,
                        Gradient4,
                        Gradient3,
                        Gradient2,
                        Gradient1,
                        feature.darkColor
                    ),
                    start = when (index) {
                        1 -> Offset(1500f, 200f)
                        2 -> Offset(80f, 90f)
                        3 -> Offset(100f, 150f)
                        4 -> Offset(1200f, 2000f)
                        else -> Offset(0f, 0f)
                    }
                )
            )
    ) {

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.h4,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.TopStart),
                color = PrimaryVeryDarkColor
            )

            Icon(
                painter = painterResource(feature.iconResource),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.Center)
            )
            Text(
                text = "Start",
                color = AccentColor,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.clickable {

                }.align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(10.dp))
                    .background(BackgroundColor)
                    .border(border = BorderStroke(2.dp, AccentColor), RoundedCornerShape(10.dp))
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}

@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = Color.White,
    inactiveText: Color = AquaBlue,
    initialSelectedItemIndex: Int = 0
) {
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBlue)
            .padding(16.dp)
    ) {
        items.forEachIndexed { index, bottomMenuContent ->
            BottomMenuItem(
                item = bottomMenuContent,
                onItemClick = {
                    selectedItemIndex = index
                },
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveText
            )
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    onItemClick: () -> Unit,
    isSelected: Boolean = false,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(item.resource),
                contentDescription = null,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = item.title,
            color = if (isSelected) activeTextColor else inactiveTextColor
        )
    }
}




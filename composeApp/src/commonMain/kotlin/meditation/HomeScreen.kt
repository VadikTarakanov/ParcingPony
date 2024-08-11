package meditation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import meditation.model.BottomMenuContent
import meditation.model.Feature
import meditation.utils.standardQuadFromTo
import org.jetbrains.compose.resources.painterResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_bubble
import prancingpony.composeapp.generated.resources.ic_headphone
import prancingpony.composeapp.generated.resources.ic_home
import prancingpony.composeapp.generated.resources.ic_moon
import prancingpony.composeapp.generated.resources.ic_music
import prancingpony.composeapp.generated.resources.ic_play
import prancingpony.composeapp.generated.resources.ic_profile
import prancingpony.composeapp.generated.resources.ic_search
import prancingpony.composeapp.generated.resources.ic_videocam
import ui.AquaBlue
import ui.Beige1
import ui.Beige2
import ui.Beige3
import ui.BlueViolet1
import ui.BlueViolet2
import ui.BlueViolet3
import ui.ButtonBlue
import ui.DarkerButtonBlue
import ui.DeepBlue
import ui.LightGreen1
import ui.LightGreen2
import ui.LightGreen3
import ui.LightRed
import ui.OrangeYellow1
import ui.OrangeYellow2
import ui.OrangeYellow3
import ui.TextWhite

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .background(DeepBlue)
            .fillMaxSize()
    ) {
        val listOfFeature = listOf(
            Feature(
                "One",
                iconResource = Res.drawable.ic_headphone,
                lightColor = BlueViolet1,
                mediumColor = BlueViolet2,
                darkColor = BlueViolet3
            ),
            Feature(
                "Two",
                iconResource = Res.drawable.ic_videocam,
                lightColor = LightGreen1,
                mediumColor = LightGreen2,
                darkColor = LightGreen3
            ),
            Feature(
                "Three",
                iconResource = Res.drawable.ic_bubble,
                lightColor = Beige1,
                mediumColor = Beige2,
                darkColor = Beige3
            ),
            Feature(
                "Four",
                iconResource = Res.drawable.ic_profile,
                lightColor = OrangeYellow3,
                mediumColor = OrangeYellow2,
                darkColor = OrangeYellow1
            )
        )
        Column {
            GreetingSection()
            ChipsSection()
            CurrentMeditation()
            FeatureSection(listOfFeature)
        }
        BottomMenu(
            items = listOf(
                BottomMenuContent("Home", Res.drawable.ic_home),
                BottomMenuContent("Meditate", Res.drawable.ic_bubble),
                BottomMenuContent("Sleep", Res.drawable.ic_moon),
                BottomMenuContent("Music", Res.drawable.ic_music),
                BottomMenuContent("Statistic", Res.drawable.ic_profile),
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good morning, $name",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "We wish you have good day!",
                style = MaterialTheme.typography.body1
            )
        }
        Icon(
            painter = painterResource(resource = Res.drawable.ic_search),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ChipsSection(list: List<String> = listOf("One", "Two", "Three")) {
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow {
        items(list.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 10.dp)
                    .clickable {
                        selectedChipIndex = it
                    }.clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedChipIndex == it) ButtonBlue
                        else DarkerButtonBlue
                    ).padding(10.dp)
            ) {
                Text(list[it], color = TextWhite)
            }
        }
    }
}

@Composable
fun CurrentMeditation(
    color: Color = LightRed
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Some interesting text",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "Another interesting text !",
                style = MaterialTheme.typography.body1,
                color = TextWhite
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ButtonBlue)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_play),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun FeatureSection(features: List<Feature>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(15.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(features.size) {
                FeatureItem(features[it])
            }
        }
    }
}

@Composable
fun FeatureItem(
    feature: Feature
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
            .background(feature.darkColor)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())
        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

//        val filledPath = Path()
//        filledPath.addPath(path = lightColoredPath)
//        filledPath.lineTo(width.toFloat(), height.toFloat())
//        filledPath.lineTo(0f, height.toFloat())
//        filledPath.close()
//
//        val brush = Brush.verticalGradient(listOf(Color.White .copy(alpha = 0.4f), Color.Transparent))
        Canvas(
            modifier = Modifier
                .fillMaxSize()

        ) {
            clipRect(right = size.width * drawPathAnimation.value) {
                drawPath(
                    path = mediumColoredPath,
                    color = feature.mediumColor
                )
                drawPath(
                    path = lightColoredPath,
                    color = feature.lightColor
                )
//                drawPath(
//                    filledPath,
//                    brush,
//                    style = Fill
//                )
            }
        }

        Box(modifier = Modifier.fillMaxSize().padding(15.dp)) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.h2,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Icon(
                painter = painterResource(feature.iconResource),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = "Start",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {

                }.align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ButtonBlue)
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
            .padding(15.dp)
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




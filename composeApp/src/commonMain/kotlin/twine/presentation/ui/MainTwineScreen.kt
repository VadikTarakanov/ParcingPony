package twine.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_play
import prancingpony.composeapp.generated.resources.ic_sports_gymnastics_24dp
import prancingpony.composeapp.generated.resources.ic_sports_martial_arts_24dp
import prancingpony.composeapp.generated.resources.ic_trophy_24dp
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

@Composable
fun MainTwineScreen() {
    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        val listOfFeature = listOf(
            Feature(
                "Transverse split",
                iconResource = Res.drawable.ic_sports_gymnastics_24dp,
                lightColor = PrimaryDarkColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryVeryLightColor
            ),
            Feature(
                "Longitudinal split",
                iconResource = Res.drawable.ic_sports_gymnastics_24dp,
                lightColor = PrimaryLightColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryLightColor
            ),
            Feature(
                "Static Leg Workout",
                iconResource = Res.drawable.ic_sports_martial_arts_24dp,
                lightColor = PrimaryVeryLightColor,
                mediumColor = PrimaryColor,
                darkColor = PrimaryDarkColor
            ),
            Feature(
                "Progress",
                iconResource = Res.drawable.ic_trophy_24dp,
                lightColor = PrimaryVeryLightColor,
                mediumColor = PrimaryLightColor,
                darkColor = PrimaryColor
            )
        )
        Column {
            GreetingSection()
            ChipsSection()
            CurrentMeditation()
            FeatureSection(listOfFeature)
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
            .padding(15.dp)
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
fun CurrentMeditation(
    color: Color = PrimaryColor
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
                .background(AccentColor)
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
            modifier = Modifier.padding(15.dp),
            color = PrimaryVeryDarkColor
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

        Box(modifier = Modifier.fillMaxSize().padding(15.dp)) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.h2,
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
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {

                }.align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AccentColor)
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




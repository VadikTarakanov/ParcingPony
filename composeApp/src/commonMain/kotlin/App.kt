import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import prancingpony.composeapp.generated.resources.NunitoSans_7pt_Condensed_Black
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.pesel
import kotlin.random.Random

@Composable
@Preview
fun App(
    batterySettings: BatterySettings
) {
    MaterialTheme {
        val scaffoldState = rememberScaffoldState()

        val scrollState = rememberLazyListState()

        var sizeState by remember { mutableStateOf(50.dp) }
        val size by animateDpAsState(
            targetValue = sizeState,
            animationSpec = tween(
                durationMillis = 2000,
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )

        val infiniteTransition = rememberInfiniteTransition()
        val color by infiniteTransition.animateColor(
            initialValue = Color.Red,
            targetValue = Color.Black,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val scope = rememberCoroutineScope()
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.padding(40.dp))
                Header("Vadim")
                val levelBattery = batterySettings.getBatteryLevel()
                Text(
                    text = "Level battery ${levelBattery}",
                    fontFamily = FontFamily(fonts = listOf(Font(resource = Res.font.NunitoSans_7pt_Condensed_Black)))
                )

                Spacer(modifier = Modifier.padding(16.dp))
                ImageCard(painter = painterResource(resource = Res.drawable.pesel), "Pesel 19 years old")
                Spacer(modifier = Modifier.padding(16.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(100) {
                        Image(
                            painter = painterResource(resource = Res.drawable.pesel),
                            contentDescription = "Pesel 19 years old",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Button(onClick = {
                    sizeState += 25.dp
                }) {
                    Text(text = "Change height of box ")
                }
                TextFieldElement(scope, scaffoldState)
                Spacer(modifier = Modifier.padding(16.dp))
            }

            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(size)
                    .background(color)
            ) {
            }

        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CustomProgressBar(0.4f, 100)
        }
    }
}

@Composable
fun Header(name: String) {
    val question = remember {
        mutableStateOf("Wat's up?")
    }
    Text(
        text = "Hi $name ${question.value}",
        fontSize = TextUnit(28F, TextUnitType.Sp),
        modifier = Modifier.clickable {
            question.value = getQuestion(Random.nextInt(0, 4))
        },
        fontFamily = FontFamily(fonts = listOf(Font(resource = Res.font.NunitoSans_7pt_Condensed_Black)))
    )
}

@Composable
fun ImageCard(
    painter: Painter,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(130.dp).height(140.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,

        ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 180f,

                            )
                    )
            )

            Box(modifier = Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.BottomCenter) {
                Text(text = title, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}

@Composable
fun TextFieldElement(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        var textFieldState by remember {
            mutableStateOf("")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            TextField(
                value = textFieldState,
                label = {
                    Text(text = "Enter your name")
                },
                onValueChange = {
                    textFieldState = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Snack bar message ")
                }
            }) {
                Text(text = "Pls greet me")
            }
        }
    }
}

@Composable
fun CustomProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animPlayed by remember {
        mutableStateOf(false)
    }

    val curPercentage = animateFloatAsState(
        targetValue = if (animPlayed) percentage else 0f,
        animationSpec = tween(durationMillis = animDuration, delayMillis = animDelay)
    )

    LaunchedEffect(key1 = true) {
        animPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360f * curPercentage.value,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getQuestion(numOfQuestion: Int): String {
    return listOf(
        "Как дела?",
        "Что нового?",
        "Какие успехи?",
        "Когда закончишь проект?",
        "Сроки уже наисходе",
    ).getOrNull(numOfQuestion) ?: "Таких вопросов нет"
}
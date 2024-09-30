package twine.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import twine.presentation.components.profile.ProfileComponent
import twine.utils.TimeMask
import ui.PrimaryLightColor
import ui.TextPrimary
import ui.TextWhite

@Composable
@Preview
fun ProfileScreen(
    component: ProfileComponent
) {
    var timeTraining by remember { mutableStateOf(component.getTimeTraining()) }
    var timeDelayBeforeTraining by remember { mutableStateOf(component.getTimeDelayTraining()) }

    val isErrorTimeTraining by component.isInvalidTimeTraining.collectAsState()
    val isErrorTimeDelay by component.isInvalidDelayTimeTraining.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 18.dp, horizontal = 16.dp),
    ) {
        TimeSettingsHeader()
        TimeTrainingTextField(
            textLabel = "Setup time training",
            textPlaceHolder = "MM:SS",
            timeData = timeTraining,
            onValueChanged = { newText ->
                if (newText.length <= TimeMask.MAX_LENGTH) {
                    component.clearStateError()
                    timeTraining = newText.filter { it.isDigit() }
                }
            },
            isError = isErrorTimeTraining
        )

        TimeTrainingTextField(
            textLabel = "Setup delay before timer starts",
            textPlaceHolder = "MM:SS",
            timeData = timeDelayBeforeTraining,
            onValueChanged = { newText ->
                if (newText.length <= TimeMask.MAX_LENGTH) {
                    component.clearStateError()
                    timeDelayBeforeTraining = newText.filter { it.isDigit() }
                }
            },
            isError = isErrorTimeDelay
        )

        Button(
            onClick = {
                val isSavedData =
                    component.updateTimeTraining(timeTraining = timeTraining, timeDelay = timeDelayBeforeTraining)
                if (isSavedData) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Settings have been saved")
                    }
                }
            },
            enabled = !isErrorTimeTraining
        ) {
            Text("save settings")
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            snackbar = { data ->
                Snackbar(
                    backgroundColor = PrimaryLightColor, // Изменение фона
                    contentColor = TextWhite,       // Изменение цвета текста
                    snackbarData = data
                )
            },
        )
    }
}

@Composable
fun TimeSettingsHeader(modifier: Modifier = Modifier) {
    Text(
        text = "Time Settings",
        modifier = modifier.padding(vertical = 16.dp),
        color = TextPrimary,
        style = MaterialTheme.typography.h1
    )
}

@Composable
fun TimeTrainingTextField(
    modifier: Modifier = Modifier,
    textLabel: String,
    timeData: String,
    onValueChanged: (String) -> Unit,
    textPlaceHolder: String,
    isError: Boolean
) {

    OutlinedTextField(
        value = timeData,
        onValueChange = onValueChanged,
        visualTransformation = TimeMask(TimeMask.TIME_MASK),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 34.sp,
        ),
        maxLines = 1,
        placeholder = {
            Text(
                text = textPlaceHolder,
                fontSize = 34.sp,
            )
        },
        label = {
            if (isError) {
                Text(text = "Minutes and seconds should be filled")
            } else Text(text = textLabel)
        },
        isError = isError,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

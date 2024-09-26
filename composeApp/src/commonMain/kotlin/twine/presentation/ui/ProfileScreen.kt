package twine.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import twine.presentation.components.profile.ProfileComponent
import twine.utils.TimeMask
import ui.SecondaryColor
import ui.TextPrimary

@Composable
@Preview
fun ProfileScreen(
    component: ProfileComponent
) {
    val profileComponent = remember { component }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 18.dp, horizontal = 16.dp),
    ) {
        TimeSettingsHeader()
        TimeTrainingTextField(
            component = profileComponent,
            textLabel = "Setup time training",
            textPlaceHolder = "MM:SS"
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
    component: ProfileComponent,
    textLabel: String,
    textPlaceHolder: String
) {
    val timeTraining = remember {
        component.getTimeTraining()
    }

    var timeData by remember { mutableStateOf(timeTraining) }
    var stateIconDone by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = timeData,
        onValueChange = { newText ->
            if (newText.length <= TimeMask.MAX_LENGTH) {
                stateIconDone = false
                timeData = newText.filter { it.isDigit() }
            }
        },
        visualTransformation = TimeMask(TimeMask.TIME_MASK),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(onDone = {
            stateIconDone = true
            sendTime(timeData, component)
        }),
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
            Text(text = textLabel)
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    stateIconDone = !stateIconDone
                    if (stateIconDone) {
                        focusRequester.freeFocus()
                        sendTime(timeData, component)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done",
                    tint = if (stateIconDone) Color.Green else SecondaryColor
                )
            }
        },
        modifier = modifier.focusRequester(focusRequester)
    )
}

private fun sendTime(time: String, component: ProfileComponent) {
    component.updateTimeTraining(timeTraining = time)
}

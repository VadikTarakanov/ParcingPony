package twine.di

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState

class CommonDependency(
    val orientationState: MutableIntState,
    val isTrainingStart: MutableState<Boolean>
)
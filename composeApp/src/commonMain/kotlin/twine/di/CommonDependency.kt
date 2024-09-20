package twine.di

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import twine.utils.TimeConverter

class CommonDependency(
    val orientationState: MutableIntState,
    val isTrainingStart: MutableState<Boolean>,
    val timeConvertor: TimeConverter
)
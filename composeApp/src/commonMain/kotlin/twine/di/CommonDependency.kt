package twine.di

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import twine.presentation.model.TypeTraining
import twine.utils.SoundPlayer
import twine.utils.TimeConverter

class CommonDependency(
    val orientationState: MutableIntState,
    val isTrainingStart: MutableState<Boolean>,
    val timeConvertor: TimeConverter,
    val soundPlayer: SoundPlayer,
    val typeTraining: TypeTraining,
    val onTrainingClick: (TypeTraining) -> Unit
)
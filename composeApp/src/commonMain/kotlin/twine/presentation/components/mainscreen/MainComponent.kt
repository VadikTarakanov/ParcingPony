package twine.presentation.components.mainscreen

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import twine.presentation.model.ResultTraining
import twine.presentation.model.TypeTraining

interface MainComponent {
    val maxScore: StateFlow<Int>

    fun getBestResultByType(typeTraining: TypeTraining): Flow<Int>
}
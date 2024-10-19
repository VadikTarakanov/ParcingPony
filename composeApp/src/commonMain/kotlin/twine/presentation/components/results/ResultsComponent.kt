package twine.presentation.components.results

import kotlinx.coroutines.flow.Flow
import twine.data.model.SideSplitProgressDto
import twine.presentation.model.TypeTraining

interface ResultsComponent {
    fun getResults(typeTraining: TypeTraining): Flow<List<SideSplitProgressDto>>

    fun deleteResult(typeTraining: TypeTraining, item: SideSplitProgressDto)
}
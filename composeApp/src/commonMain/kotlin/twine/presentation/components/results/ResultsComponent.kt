package twine.presentation.components.results

import kotlinx.coroutines.flow.Flow
import twine.data.model.SideSplitProgressDto

interface ResultsComponent {
    fun getResults(): Flow<List<SideSplitProgressDto>>

    fun deleteResult(item : SideSplitProgressDto)
}
package twine.presentation.components.results

import kotlinx.coroutines.flow.Flow
import twine.data.ResultsRepository
import twine.data.model.SideSplitProgressDto

class ResultsComponentImpl(
    private val resultsRepository: ResultsRepository
) : ResultsComponent {

    override fun getResults(): Flow<List<SideSplitProgressDto>> {
        return resultsRepository
            .getAllResults()
    }

    override fun deleteResult(item: SideSplitProgressDto) {
        item.id?.let { resultsRepository.deleteResult(it) }
    }
}
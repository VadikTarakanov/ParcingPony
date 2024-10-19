package twine.presentation.components.results

import kotlinx.coroutines.flow.Flow
import twine.data.ResultsRepository
import twine.data.model.SideSplitProgressDto
import twine.presentation.model.TypeTraining

class ResultsComponentImpl(
    private val resultsRepository: ResultsRepository
) : ResultsComponent {

    override fun getResults(typeTraining: TypeTraining): Flow<List<SideSplitProgressDto>> {
        return resultsRepository
            .getAllResults(typeTraining)
    }

    override fun deleteResult(typeTraining: TypeTraining, item: SideSplitProgressDto) {
        item.id?.let { resultsRepository.deleteResult(typeTraining = typeTraining, id = it) }
    }
}
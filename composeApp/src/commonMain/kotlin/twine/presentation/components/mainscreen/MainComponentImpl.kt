package twine.presentation.components.mainscreen

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import twine.data.ResultsRepository
import twine.presentation.model.TypeTraining

class MainComponentImpl(
    componentContext: ComponentContext,
    private val resultsRepository: ResultsRepository,
) : MainComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val _maxScore: MutableStateFlow<Int> = MutableStateFlow<Int>(0)
    override val maxScore: StateFlow<Int> = _maxScore.asStateFlow()

    override fun getBestResultByType(typeTraining: TypeTraining): Flow<Int> {
        return resultsRepository.getAllResults(typeTraining).map { sideSplitProgressDtos ->
            sideSplitProgressDtos.maxOfOrNull { maxResult ->
                return@maxOfOrNull maxResult.progress
            } ?: 0
        }
    }
}
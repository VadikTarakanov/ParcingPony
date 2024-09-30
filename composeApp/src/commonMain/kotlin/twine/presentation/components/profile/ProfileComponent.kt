package twine.presentation.components.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {
    val isInvalidTimeTraining: StateFlow<Boolean>
    val isInvalidDelayTimeTraining: StateFlow<Boolean>

    fun clearStateError()
    fun updateTimeTraining(timeTraining: String, timeDelay: String) : Boolean
    fun getTimeTraining(): String
    fun getTimeDelayTraining(): String
}
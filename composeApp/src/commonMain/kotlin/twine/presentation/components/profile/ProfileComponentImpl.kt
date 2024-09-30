package twine.presentation.components.profile

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import twine.data.TimerSettingsRepository

class ProfileComponentImpl(
    componentContext: ComponentContext,
    private val timerSettingsRepository: TimerSettingsRepository
) : ProfileComponent, ComponentContext by componentContext {

    private val _isInvalidTimeTraining = MutableStateFlow(false)
    override val isInvalidTimeTraining: StateFlow<Boolean> = _isInvalidTimeTraining.asStateFlow()

    private val _isInvalidDelayTimeTraining = MutableStateFlow(false)
    override val isInvalidDelayTimeTraining: StateFlow<Boolean> = _isInvalidDelayTimeTraining.asStateFlow()

    override fun clearStateError() {
        _isInvalidTimeTraining.value = false
        _isInvalidDelayTimeTraining.value = false
    }

    override fun updateTimeTraining(timeTraining: String, timeDelay: String): Boolean {
        val timeOfTraining = parseTime(timeTraining)
        val timeOfDelay = parseTime(timeDelay)

        val minutesTimeTrainingParsed = try {
            timeOfTraining.first.toLong()
        } catch (e: Exception) {
            _isInvalidTimeTraining.value = true
            return false
        }

        val secondsTimeTrainingParsed = try {
            timeOfTraining.second.toLong()
        } catch (e: Exception) {
            _isInvalidTimeTraining.value = true
            return false
        }

        val minutesDelayParsed = try {
            timeOfDelay.first.toLong()
        } catch (e: Exception) {
            _isInvalidDelayTimeTraining.value = true
            return false
        }

        val secondsDelayParsed = try {
            timeOfDelay.second.toLong()
        } catch (e: Exception) {
            _isInvalidDelayTimeTraining.value = true
            return false
        }

        timerSettingsRepository.setTimerSettings(
            mainTimeMinutes = minutesTimeTrainingParsed,
            mainTimeSeconds = secondsTimeTrainingParsed,
            delayTimeMinutes = minutesDelayParsed,
            delayTimeSeconds = secondsDelayParsed
        )
        return true
    }

    override fun getTimeTraining(): String {
        val timePair = timerSettingsRepository.getTimerSettings()
        val minutes = timePair.mainTimeMinutes.toString()
        val seconds = timePair.mainTimeSeconds.toString()

        val updatedMinutes = if (minutes.length < 2) {
            "0$minutes"
        } else minutes

        val updatedSeconds = if (seconds.length < 2) {
            "0$seconds"
        } else seconds

        return "$updatedMinutes$updatedSeconds"
    }

    override fun getTimeDelayTraining(): String {
        val timePair = timerSettingsRepository.getTimerSettings()
        val minutes = timePair.delayTimeMinutes.toString()
        val seconds = timePair.delayTimeSeconds.toString()

        val updatedMinutes = if (minutes.length < 2) {
            "0$minutes"
        } else minutes

        val updatedSeconds = if (seconds.length < 2) {
            "0$seconds"
        } else seconds

        return "$updatedMinutes$updatedSeconds"
    }

    private fun parseTime(time: String): Pair<String, String> {
        val minutes = StringBuilder()
        val seconds = StringBuilder()
        time.forEachIndexed { index, element ->
            if (index <= 1) {
                minutes.append(element)
            } else {
                seconds.append(element)
            }
        }

        return Pair(minutes.toString(), seconds.toString())
    }
}
package twine.presentation.components.profile

import com.arkivanov.decompose.ComponentContext
import twine.data.TimerRepository

class ProfileComponentImpl(
    componentContext: ComponentContext,
    private val timerRepository: TimerRepository
) : ProfileComponent, ComponentContext by componentContext {

    override fun updateTimeTraining(timeTraining: String) {
        val minutes = StringBuilder()
        val seconds = StringBuilder()
        timeTraining.forEachIndexed { index, element ->
            if (index <= 1) {
                minutes.append(element)
            } else {
                seconds.append(element)
            }
        }
        timerRepository.setTimeTraining(
            Pair(minutes.toString().toLong(), seconds.toString().toLong())
        )
    }

    override fun getTimeTraining(): String {
        val timePair = timerRepository.getTimeTraining()
        val minutes = timePair.first.toString()
        val seconds = timePair.second.toString()

        val updatedMinutes = if (minutes.length < 2) {
            "0$minutes"
        } else minutes

        val updatedSeconds = if (seconds.length < 2) {
            "0$seconds"
        } else seconds

        return "$updatedMinutes$updatedSeconds"
    }
}
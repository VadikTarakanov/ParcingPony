package twine.data

class TimerRepository {

    fun setTimeTraining(time: Pair<Long, Long>) {
        timeOfTrainingMinutes = time.first
        timeOfTrainingSeconds = time.second
    }

    fun getTimeTraining(): Pair<Long, Long> {
        return Pair(timeOfTrainingMinutes, timeOfTrainingSeconds)
    }

    private companion object {
        var timeOfTrainingSeconds: Long = 9
        var timeOfTrainingMinutes: Long = 0
    }
}
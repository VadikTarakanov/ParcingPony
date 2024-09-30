package twine.data

import sqldelight.sqldelight.twin.data.SplitDatabase
import sqldelight.twin.data.TimerSettings

class TimerSettingsRepository(
    database: SplitDatabase
) {

    private val queries = database.timerSettingsQueries

    fun setTimerSettings(
        mainTimeMinutes: Long,
        mainTimeSeconds: Long,
        delayTimeMinutes: Long,
        delayTimeSeconds: Long
    ) {
        queries.replaceItem(
            TimerSettings(
                //const value
                id = 1,
                mainTimeMinutes = mainTimeMinutes,
                mainTimeSeconds = mainTimeSeconds,
                delayTimeMinutes = delayTimeMinutes,
                delayTimeSeconds = delayTimeSeconds
            )
        )
    }

    fun getTimerSettings(): TimerSettings {
        return try {
            println("try to get data from db ")
            queries.selectAll().executeAsList().getOrNull(0) ?: TimerSettings(
                id = 1,
                mainTimeMinutes = 0,
                mainTimeSeconds = 59,
                delayTimeMinutes = 0,
                delayTimeSeconds = 10
            )
        } catch (e: Exception) {
            println("Some Error happen get default value")
            TimerSettings(
                id = 1,
                mainTimeMinutes = 0,
                mainTimeSeconds = 59,
                delayTimeMinutes = 0,
                delayTimeSeconds = 10
            )
        }
    }
}
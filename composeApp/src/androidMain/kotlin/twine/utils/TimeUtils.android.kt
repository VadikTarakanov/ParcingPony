package twine.utils

import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual class TimeConverter {
    actual fun convertMillisecondsToFormatSeconds(millis: Long): String {
        val duration = millis.toDuration(DurationUnit.MILLISECONDS)
        val timeString =
            duration.toComponents { minutes, seconds, _ ->
                String.format("%02d:%02d", minutes, seconds)
            }
        return timeString
    }
}
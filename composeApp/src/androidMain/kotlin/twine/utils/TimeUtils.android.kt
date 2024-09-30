package twine.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    actual fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
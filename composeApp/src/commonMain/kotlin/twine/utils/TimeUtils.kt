package twine.utils

expect class TimeConverter {

    fun convertMillisecondsToFormatSeconds(millis: Long): String

    fun getCurrentDateTime(): String
}
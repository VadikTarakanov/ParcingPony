package twine.utils

expect class SystemTime {
    companion object {
        fun getCurrentTime(): Long
    }
}
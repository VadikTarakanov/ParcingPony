package twine.utils

actual class SystemTime {
    actual companion object {
        actual fun getCurrentTime(): Long {
           return System.currentTimeMillis()
        }
    }
}
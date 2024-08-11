import oshi.SystemInfo
import kotlin.math.roundToInt

actual class BatterySettings {
    actual fun getBatteryLevel(): Int {
        val systemInfo = SystemInfo()
        val batteryLevel = systemInfo.hardware.powerSources.firstOrNull()
        return batteryLevel?.remainingCapacityPercent?.times(100)?.roundToInt() ?: -1
    }
}
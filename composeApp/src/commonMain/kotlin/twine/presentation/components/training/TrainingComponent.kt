package twine.presentation.components.training

import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.flow.StateFlow

interface TrainingComponent {
    val timer: StateFlow<Float>
    val statePermission: StateFlow<PermissionState>

    fun openAppSettings()

    fun requestPermissionCamera()
}
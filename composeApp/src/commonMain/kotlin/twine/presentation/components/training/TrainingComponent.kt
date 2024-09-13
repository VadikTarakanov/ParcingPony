package twine.presentation.components.training

import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.flow.StateFlow
import twine.data.model.SplitProgressModel

interface TrainingComponent {
    val resultInPercent: StateFlow<Float>
    val statePermission: StateFlow<PermissionState>

    fun openAppSettings()

    fun requestPermissionCamera()

    fun saveSplitResult(model: SplitProgressModel, isEndTraining: Boolean)

    fun storeSplitResult()
}
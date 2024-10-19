package twine.presentation.components.training

import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.flow.StateFlow
import twine.data.model.SplitProgressModel
import twine.presentation.model.ResultTraining
import twine.presentation.model.TypeTraining

interface TrainingComponent {
    val resultTraining: StateFlow<ResultTraining?>
    val statePermission: StateFlow<PermissionState>
    val isShowResult: StateFlow<Boolean>

    fun openAppSettings()

    fun requestPermissionCamera()

    fun saveSplitResult(
        model: SplitProgressModel,
        isEndTraining: Boolean,
        typeTraining: TypeTraining
    )

    fun storeSplitResult()

    fun getTimeTraining(): Long

    fun getTimerDelay(): Long
}
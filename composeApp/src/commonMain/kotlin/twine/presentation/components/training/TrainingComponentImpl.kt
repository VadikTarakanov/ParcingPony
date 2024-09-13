package twine.presentation.components.training

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import twine.data.model.SplitProgressModel

class TrainingComponentImpl(
    private val component: ComponentContext,
    private val permissionsController: PermissionsController
) : TrainingComponent, ComponentContext by component {
    private val _resultInPercent = MutableStateFlow(0.0f)
    override val resultInPercent = _resultInPercent.asStateFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private val _statePermission = MutableStateFlow(PermissionState.NotDetermined)
    override val statePermission = _statePermission.asStateFlow()

    private val splitResults = mutableListOf<SplitProgressModel>()

    init {
        coroutineScope.launch {
            _statePermission.value = permissionsController.getPermissionState(Permission.CAMERA)
            println("inital State ${_statePermission.value}")
        }
    }

    override fun openAppSettings() {
        permissionsController.openAppSettings()
    }

    override fun requestPermissionCamera() {
        coroutineScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _statePermission.value = PermissionState.Granted
                println("TrainingScreen ViewModel Granted ${_statePermission.value}")
                startTimer()
            } catch (e: DeniedAlwaysException) {
                _statePermission.value = PermissionState.DeniedAlways
                println("TrainingScreen ViewModel DeniedAlways ${_statePermission.value}")
            } catch (e: DeniedException) {
                _statePermission.value = PermissionState.Denied
                println("TrainingScreen ViewModel Denied ${_statePermission.value}")
            } catch (e: RequestCanceledException) {
                _statePermission.value = PermissionState.Denied
                println("TrainingScreen ViewModel Denied  Error $e ${_statePermission.value}")
                e.printStackTrace()
            }
        }
    }

    override fun saveSplitResult(model: SplitProgressModel, isEndTraining: Boolean) {
        println("saveSplitResult $isEndTraining")
        splitResults.add(model)
    }

    override fun storeSplitResult() {
        println("---------------------------------------------------------------")
        println("splitResults SIZE ${splitResults.size}")
        val maxResult = splitResults
            .mapNotNull { it.averageAngle() }
            .maxOrNull() ?: return
        println("splitResults max Result ${maxResult}")

        splitResults.clear()

        _resultInPercent.value = calculatePercent(maxResult).toFloat()
    }

    private fun startTimer() {
        coroutineScope.launch {
            while (true) {
                delay(1000)
                _resultInPercent.value += 0.1f
            }
        }
    }

    private fun SplitProgressModel.averageAngle(): Double? {
        return if (rightAngle != null && leftAngle != null) {
            (rightAngle + leftAngle) / 2
        } else {
            null
        }
    }

    private fun calculatePercent(inputValue: Double): Double {
        val inputValueInPercents = (inputValue / PERCENT_100) * 100
        return inputValueInPercents
    }

    companion object {
        private const val PERCENT_100 = 180
    }

}
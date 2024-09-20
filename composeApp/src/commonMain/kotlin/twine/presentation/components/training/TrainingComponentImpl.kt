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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.new_record
import prancingpony.composeapp.generated.resources.same_record
import twine.data.TimerRepository
import twine.data.model.SplitProgressModel
import twine.presentation.model.ResultTraining
import twine.resourceprovider.ResourceProvider

class TrainingComponentImpl(
    private val component: ComponentContext,
    private val permissionsController: PermissionsController,
    private val timerRepository: TimerRepository,
    private val resourceProvider: ResourceProvider
) : TrainingComponent, ComponentContext by component {
    private val _resultTraining = MutableStateFlow<ResultTraining?>(null)
    override val resultTraining = _resultTraining.asStateFlow()

    private val _isShowResult = MutableStateFlow(false)
    override val isShowResult: StateFlow<Boolean> = _isShowResult.asStateFlow()

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
        println("saveSplit Result $isEndTraining")
        splitResults.add(model)
    }

    override fun storeSplitResult() {
        println("---------------------------------------------------------------")
        println("splitResults SIZE ${splitResults.size}")
        if (splitResults.size < 10) return
        val average = splitResults.map {
            it.averageAngle()
        }.getAverageFromListResults()

        // Определяем границы для выбросов
        val lowerBound = average - (average * THRESHOLD / 100)
        val upperBound = average + (average * THRESHOLD / 100)

        val maxResult = splitResults
            .mapNotNull { it.averageAngle() }
            .filter { it in lowerBound..upperBound }
            .maxOrNull() ?: return
        println("splitResults max Result ${maxResult}")

        splitResults.clear()

        val percentResult = calculatePercent(maxResult).toFloat()

        coroutineScope.launch {
            val model = getResultTrainingModel(percentResult)
            _resultTraining.value = model

            delay(TIME_TO_DELAY_WHEN_CAMERA_RESTORE)
            _isShowResult.value = true

            delay(TIME_TO_SHOWING_RESULT)
            _isShowResult.value = false
        }
    }

    override fun getTimeTraining(): Long {
        val result = timerRepository.getTimeTraining()
        val minutes = convertMinutesToSeconds(result.first)
        val seconds = result.second
        val generalTime = minutes + seconds

        return generalTime
    }

    private fun convertMinutesToSeconds(minutes: Long) = minutes * 60

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

    private suspend fun getResultTrainingModel(currentResult: Float): ResultTraining {
        val previousResult = 90 // TODO recordRepository.getCurrentRecord()

        return if (currentResult >= previousResult) {
            ResultTraining.CurrentResult(
                percentResult = currentResult,
                message = resourceProvider.getStringValue(Res.string.new_record)
            )
        } else {
            ResultTraining.CurrentResult(
                percentResult = currentResult,
                message = resourceProvider.getStringValue(Res.string.same_record)
            )
        }
    }

    private fun List<Double?>.getAverageFromListResults(): Float {
        var sum = 0.0
        this.forEach {
            if (it != null) {
                sum += it
            }
        }
        val average = sum / this.size
        return average.toFloat()
    }

    companion object {
        private const val PERCENT_100 = 180
        private const val THRESHOLD = 20
        private const val TIME_TO_SHOWING_RESULT = 15000L
        private const val TIME_TO_DELAY_WHEN_CAMERA_RESTORE = 100L
    }
}
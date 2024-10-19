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
import prancingpony.composeapp.generated.resources.failed_record
import prancingpony.composeapp.generated.resources.new_record
import prancingpony.composeapp.generated.resources.same_record
import twine.data.ResultsRepository
import twine.data.TimerSettingsRepository
import twine.data.model.SideSplitProgressDto
import twine.data.model.SplitProgressModel
import twine.data.model.TypeSplit
import twine.presentation.model.ResultTraining
import twine.presentation.model.TypeTraining
import twine.resourceprovider.ResourceProvider
import twine.utils.TimeConverter

class TrainingComponentImpl(
    private val component: ComponentContext,
    private val permissionsController: PermissionsController,
    private val timerSettingsRepository: TimerSettingsRepository,
    private val resourceProvider: ResourceProvider,
    private val resultsRepository: ResultsRepository,
    private val timerConverter: TimeConverter
) : TrainingComponent, ComponentContext by component {
    private val _resultTraining = MutableStateFlow<ResultTraining?>(null)
    override val resultTraining = _resultTraining.asStateFlow()

    private val _isShowResult = MutableStateFlow(false)
    override val isShowResult: StateFlow<Boolean> = _isShowResult.asStateFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private val _statePermission = MutableStateFlow(PermissionState.NotDetermined)
    override val statePermission = _statePermission.asStateFlow()

    private val splitResults = mutableListOf<SplitProgressModel>()

    private var _typeTraining = TypeTraining.SIDE_SPLIT

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

    override fun saveSplitResult(
        model: SplitProgressModel,
        isEndTraining: Boolean,
        typeTraining: TypeTraining
    ) {
        println("saveSplit Result $isEndTraining")
        _typeTraining = typeTraining
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
            .maxOrNull()
        println("splitResults max Result ${maxResult}")

        splitResults.clear()

        val percentResult = calculatePercent(maxResult)?.toFloat()

        percentResult?.let {
            resultsRepository.insertResult(
                SideSplitProgressDto(
                    progress = percentResult.toInt(),
                    dateTraining = timerConverter.getCurrentDateTime(),
                    splitType = when (_typeTraining) {
                        TypeTraining.SIDE_SPLIT -> TypeSplit.SIDE
                        TypeTraining.LONGITUDINAL_SPLIT_LEFT -> TypeSplit.LEFT
                        TypeTraining.LONGITUDINAL_SPLIT_RIGHT -> TypeSplit.RIGHT
                        TypeTraining.STATIC_LEG_WORKOUT -> TypeSplit.LEG_WORKOUT
                    }
                ),
                typeTraining = _typeTraining
            )
        }

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
        val result = timerSettingsRepository.getTimerSettings()
        val minutes = convertMinutesToSeconds(result.mainTimeMinutes ?: 0)
        val seconds = result.mainTimeSeconds ?: 59
        val generalTime = minutes + seconds

        return generalTime
    }

    override fun getTimerDelay(): Long {
        val result = timerSettingsRepository.getTimerSettings()
        val minutes = convertMinutesToSeconds(result.delayTimeMinutes ?: 0)
        val seconds = result.delayTimeSeconds ?: 10
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

    private fun calculatePercent(inputValue: Double?): Double? {
        if (inputValue == null) return null
        val inputValueInPercents = (inputValue / PERCENT_100) * 100
        return inputValueInPercents
    }

    private suspend fun getResultTrainingModel(currentResult: Float?): ResultTraining {
        val previousResult = 90 // TODO recordRepository.getCurrentRecord()

        return when {
            currentResult == null -> {
                ResultTraining.Failed(
                    message = resourceProvider.getStringValue(Res.string.failed_record)
                )
            }

            currentResult >= previousResult -> {
                ResultTraining.CurrentResult(
                    percentResult = currentResult,
                    message = resourceProvider.getStringValue(Res.string.new_record)
                )
            }

            else -> {
                ResultTraining.CurrentResult(
                    percentResult = currentResult,
                    message = resourceProvider.getStringValue(Res.string.same_record)
                )
            }
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
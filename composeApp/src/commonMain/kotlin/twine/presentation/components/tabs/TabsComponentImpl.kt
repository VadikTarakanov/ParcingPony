package twine.presentation.components.tabs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.serialization.Serializable
import twine.data.ResultsRepository
import twine.data.TimerSettingsRepository
import twine.di.CommonDiComponent
import twine.presentation.components.mainscreen.MainComponentImpl
import twine.presentation.components.profile.ProfileComponentImpl
import twine.presentation.components.results.ResultsComponentImpl
import twine.presentation.components.training.TrainingComponentImpl
import twine.utils.TimeConverter

class TabsComponentImpl(
    componentContext: ComponentContext,
    private val permissionsController: PermissionsController,
    private val timerSettingsRepository: TimerSettingsRepository,
    private val resultsRepository: ResultsRepository,
    private val timeConverter: TimeConverter
) : TabsComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, TabsComponent.Child>> =
        childStack(
            source = nav,
            initialConfiguration = Config.Meditation,
            serializer = Config.serializer(),
            childFactory = ::child
        )

    private fun child(config: Config, componentContext: ComponentContext): TabsComponent.Child =
        when (config) {
            is Config.Meditation -> TabsComponent.Child.MeditationChild(
                component = MainComponentImpl(
                    componentContext = componentContext,
                    resultsRepository = resultsRepository
                )
            )

            is Config.Profile -> TabsComponent.Child.ProfileChild(
                component = ProfileComponentImpl(
                    componentContext = componentContext,
                    timerSettingsRepository = timerSettingsRepository
                )
            )

            is Config.Training -> TabsComponent.Child.TrainingChild(
                component = TrainingComponentImpl(
                    component = componentContext,
                    permissionsController = permissionsController,
                    timerSettingsRepository = timerSettingsRepository,
                    resourceProvider = CommonDiComponent.getResourceProvider(),
                    resultsRepository = resultsRepository,
                    timerConverter = timeConverter
                )
            )

            is Config.Results -> TabsComponent.Child.ResultsChild(
                component = ResultsComponentImpl(
                    resultsRepository = resultsRepository
                )
            )
        }

    override fun onMeditationClicked() {
        nav.bringToFront(Config.Meditation)
    }

    override fun onProfileClicked() {
        nav.bringToFront(Config.Profile)
    }

    override fun onTrainingClicked() {
        nav.bringToFront(Config.Training)
    }

    override fun onResultsClicked() {
        nav.bringToFront(Config.Results)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Meditation : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object Training : Config

        @Serializable
        data object Results : Config
    }
}
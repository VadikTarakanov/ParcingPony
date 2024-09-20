package twine.presentation.components.tabs

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.serialization.Serializable
import twine.data.TimerRepository
import twine.di.CommonDiComponent
import twine.presentation.components.mainscreen.MainComponentImpl
import twine.presentation.components.profile.ProfileComponentImpl
import twine.presentation.components.training.TrainingComponentImpl

class TabsComponentImpl(
    componentContext: ComponentContext,
    private val permissionsController: PermissionsController,
    private val timerRepository: TimerRepository
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
                    componentContext
                )
            )

            is Config.Profile -> TabsComponent.Child.ProfileChild(
                component = ProfileComponentImpl(
                    componentContext,
                    timerRepository = timerRepository
                )
            )

            is Config.Training -> TabsComponent.Child.TrainingChild(
                component = TrainingComponentImpl(
                    component = componentContext,
                    permissionsController = permissionsController,
                    timerRepository = timerRepository,
                    resourceProvider = CommonDiComponent.getResourceProvider()
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

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Meditation : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object Training : Config
    }
}
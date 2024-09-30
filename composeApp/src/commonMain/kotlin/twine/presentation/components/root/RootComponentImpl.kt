package twine.presentation.components.root

import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.serialization.Serializable
import twine.di.CommonDiComponent
import twine.presentation.components.tabs.TabsComponentImpl
import twine.utils.TimeConverter

class RootComponentImpl(
    componentContext: ComponentContext,
    private val permissionsController: PermissionsController,
    //TODO refactor it with the koin
    private val driver: SqlDriver,
    private val timeConverter: TimeConverter
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        childFactory = ::child,
        initialConfiguration = Config.Tabs
    )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Tabs ->
                RootComponent.Child.TabsChild(
                    TabsComponentImpl(
                        componentContext = componentContext,
                        permissionsController = permissionsController,
                        timerSettingsRepository = CommonDiComponent.getTimerSettingsRepository(sqlDriver = driver),
                        resultsRepository = CommonDiComponent.getResultsRepository(sqlDriver = driver),
                        timeConverter = timeConverter
                    )
                )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Tabs : Config
    }
}
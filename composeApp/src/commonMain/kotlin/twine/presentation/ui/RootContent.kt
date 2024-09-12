package twine.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import twine.di.CommonDependency
import twine.presentation.components.root.RootComponent
import ui.MeditationUIYouTubeTheme

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
    cameraScreen: CameraScreen,
    commonDependency: CommonDependency
) {
    MeditationUIYouTubeTheme {

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        ChildrenCompose(
            component = component,
            modifier = modifier,
            cameraScreen = cameraScreen,
            commonDependency = commonDependency
        )
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ChildrenCompose(
    component: RootComponent,
    modifier: Modifier = Modifier,
    cameraScreen: CameraScreen,
    commonDependency: CommonDependency
) {
    Children(
        stack = component.stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            animation = stackAnimation(fade(minAlpha = 0.8f)),
            onBack = {}
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            when (val child = it.instance) {
                is RootComponent.Child.TabsChild -> TabsContent(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    cameraScreen = cameraScreen,
                    commonDependency = commonDependency
                )
            }
        }
    }
}
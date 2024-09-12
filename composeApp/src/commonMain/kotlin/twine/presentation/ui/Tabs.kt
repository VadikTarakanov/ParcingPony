package twine.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import twine.di.CommonDependency
import twine.presentation.components.tabs.TabsComponent

@Composable
internal fun TabsContent(
    component: TabsComponent,
    modifier: Modifier = Modifier,
    cameraScreen: CameraScreen,
    commonDependency: CommonDependency
) {
    Column(modifier = modifier) {
        ChildrenUI(
            component = component,
            modifier = Modifier.weight(1F).consumeWindowInsets(WindowInsets.navigationBars),
            cameraScreen = cameraScreen,
            commonDependency = commonDependency
        )
        BottomBar(
            component = component,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChildrenUI(
    component: TabsComponent,
    modifier: Modifier = Modifier,
    cameraScreen: CameraScreen,
    commonDependency: CommonDependency
) {
    Children(
        stack = component.stack,
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is TabsComponent.Child.MeditationChild -> MainTwineScreen()
            is TabsComponent.Child.ProfileChild -> ProfileScreen()
            is TabsComponent.Child.TrainingChild -> TrainingScreen(
                component = child.component,
                cameraScreen = cameraScreen,
                commonDependency = commonDependency
            )
        }
    }
}

@Composable
private fun BottomBar(component: TabsComponent, modifier: Modifier = Modifier) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance

    BottomNavigation(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primarySurface)
            .navigationBarsPadding(),
        elevation = 0.dp,
    ) {
        BottomNavigationItem(
            selected = activeComponent is TabsComponent.Child.MeditationChild,
            onClick = component::onMeditationClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            },
        )

        BottomNavigationItem(
            selected = activeComponent is TabsComponent.Child.ProfileChild,
            onClick = component::onProfileClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Counters",
                )
            },
        )

        BottomNavigationItem(
            selected = activeComponent is TabsComponent.Child.TrainingChild,
            onClick = component::onTrainingClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Counters",
                )
            },
        )
    }
}
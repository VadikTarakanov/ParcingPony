package twine.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import org.jetbrains.compose.resources.painterResource
import prancingpony.composeapp.generated.resources.Res
import prancingpony.composeapp.generated.resources.ic_home_24dp
import prancingpony.composeapp.generated.resources.ic_home_outline_24dp
import prancingpony.composeapp.generated.resources.ic_progress_filled_24dp
import prancingpony.composeapp.generated.resources.ic_progress_outline_24dp
import prancingpony.composeapp.generated.resources.ic_settings_outline24dp
import prancingpony.composeapp.generated.resources.ic_sports_gymnastics_24dp
import prancingpony.composeapp.generated.resources.ic_trophy_24dp
import prancingpony.composeapp.generated.resources.settings_filled_24dp
import twine.di.CommonDependency
import twine.presentation.components.tabs.TabsComponent
import ui.PrimaryLightColor
import ui.PrimaryVeryDarkColor
import ui.PrimaryVeryLightColor
import ui.SecondaryColor

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
            is TabsComponent.Child.MeditationChild -> MainTwineScreen(
                component = child.component
            )
            is TabsComponent.Child.ProfileChild -> ProfileScreen(
                component = child.component
            )
            is TabsComponent.Child.TrainingChild -> TrainingScreen(
                component = child.component,
                cameraScreen = cameraScreen,
                commonDependency = commonDependency
            )
            is TabsComponent.Child.ResultsChild -> ResultsScreen(
                component = child.component
            )
        }
    }
}

@Composable
private fun BottomBar(component: TabsComponent, modifier: Modifier = Modifier) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance

    val selectedModifier = Modifier
        .background(
            color = PrimaryLightColor,
            shape = RoundedCornerShape(size = 8.dp),
        )

    val unselectedModifier = Modifier

    BottomNavigation(
        modifier = modifier
            .fillMaxWidth()
            .background(SecondaryColor)
            .navigationBarsPadding(),
        elevation = 0.dp,
        backgroundColor = PrimaryVeryLightColor
    ) {
        BottomNavigationItem(
            modifier = Modifier.padding(top = 6.dp),
            selected = activeComponent is TabsComponent.Child.MeditationChild,
            onClick = component::onMeditationClicked,
            icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = if (activeComponent is TabsComponent.Child.MeditationChild) selectedModifier else unselectedModifier
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            painter = painterResource(resource = if (activeComponent is TabsComponent.Child.MeditationChild) Res.drawable.ic_home_24dp else Res.drawable.ic_home_outline_24dp),
                            contentDescription = "Home",
                            tint = if (activeComponent is TabsComponent.Child.MeditationChild) PrimaryVeryDarkColor else SecondaryColor
                        )
                    }

                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.subtitle2,
                        color = if (activeComponent is TabsComponent.Child.MeditationChild) PrimaryVeryDarkColor else SecondaryColor
                    )
                }
            },
        )

        BottomNavigationItem(
            modifier = Modifier.padding(top = 6.dp),
            selected = activeComponent is TabsComponent.Child.TrainingChild,
            onClick = component::onTrainingClicked,
            icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = if (activeComponent is TabsComponent.Child.TrainingChild) selectedModifier else unselectedModifier
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            painter = painterResource(resource = Res.drawable.ic_sports_gymnastics_24dp),
                            contentDescription = "Training",
                            tint = if (activeComponent is TabsComponent.Child.TrainingChild) PrimaryVeryDarkColor else SecondaryColor
                        )
                    }

                    Text(
                        text = "Training",
                        style = MaterialTheme.typography.subtitle2,
                        color = if (activeComponent is TabsComponent.Child.TrainingChild) PrimaryVeryDarkColor else SecondaryColor
                    )
                }
            },
        )

        BottomNavigationItem(
            modifier = Modifier.padding(top = 6.dp),
            selected = activeComponent is TabsComponent.Child.ResultsChild,
            onClick = component::onResultsClicked,
            icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = if (activeComponent is TabsComponent.Child.ResultsChild) selectedModifier else unselectedModifier
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            painter = painterResource(resource = if (activeComponent is TabsComponent.Child.ResultsChild) Res.drawable.ic_progress_filled_24dp else Res.drawable.ic_progress_outline_24dp),
                            contentDescription = "Progress",
                            tint = if (activeComponent is TabsComponent.Child.ResultsChild) PrimaryVeryDarkColor else SecondaryColor
                        )
                    }

                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.subtitle2,
                        color = if (activeComponent is TabsComponent.Child.ResultsChild) PrimaryVeryDarkColor else SecondaryColor
                    )
                }
            }
        )

        BottomNavigationItem(
            modifier = Modifier.padding(top = 6.dp),
            selected = activeComponent is TabsComponent.Child.ProfileChild,
            onClick = component::onProfileClicked,
            icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = if (activeComponent is TabsComponent.Child.ProfileChild) selectedModifier else unselectedModifier
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            painter = painterResource(resource = if (activeComponent is TabsComponent.Child.ProfileChild) Res.drawable.settings_filled_24dp else Res.drawable.ic_settings_outline24dp),
                            contentDescription = "Settings",
                            tint = if (activeComponent is TabsComponent.Child.ProfileChild) PrimaryVeryDarkColor else SecondaryColor
                        )
                    }

                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.subtitle2,
                        color = if (activeComponent is TabsComponent.Child.ProfileChild) PrimaryVeryDarkColor else SecondaryColor
                    )
                }
            }

        )
    }
}
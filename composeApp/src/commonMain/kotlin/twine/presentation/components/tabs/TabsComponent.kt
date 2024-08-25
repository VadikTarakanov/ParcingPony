package twine.presentation.components.tabs

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import twine.presentation.components.mainscreen.MainComponent
import twine.presentation.components.profile.ProfileComponent
import twine.presentation.components.training.TrainingComponent

interface TabsComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onMeditationClicked()
    fun onProfileClicked()
    fun onTrainingClicked()

    sealed class Child {
        data class MeditationChild(val component: MainComponent) : Child()
        data class ProfileChild(val component: ProfileComponent) : Child()
        data class TrainingChild(val component: TrainingComponent) : Child()
    }
}

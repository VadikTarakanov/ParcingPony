package org.cyberpony.prancingpony

import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import twine.data.DriverFactory
import twine.di.CommonDependency
import twine.presentation.components.root.RootComponentImpl
import twine.presentation.ui.CameraScreen
import twine.presentation.ui.RootContent
import twine.utils.SoundPlayer
import twine.utils.TimeConverter

class MainActivity : ComponentActivity() {

    private var rotationState = mutableIntStateOf(Surface.ROTATION_0)

    private var isTrainingStart = mutableStateOf(false)

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                rotationState.intValue = rotation
            }
        }
    }

    private val driverFactory by lazy {
        DriverFactory(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            val root =
                RootComponentImpl(
                    componentContext = defaultComponentContext(),
                    permissionsController = controller,
                    driver = driverFactory.createDriver(),
                    timeConverter = TimeConverter()
                )

            BindEffect(controller)
            RootContent(
                modifier = Modifier.systemBarsPadding(),
                component = root,
                cameraScreen = CameraScreen(isTrainingStart = isTrainingStart),
                commonDependency = CommonDependency(
                    orientationState = rotationState,
                    isTrainingStart = isTrainingStart,
                    timeConvertor = TimeConverter(),
                    soundPlayer = SoundPlayer(this)
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // App(this)
}
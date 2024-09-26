package twine.utils

import android.content.Context
import android.media.MediaPlayer
import org.cyberpony.prancingpony.R

actual class SoundPlayer(
    private val context: Context
) {
    private val soundStartTraining by lazy {
        MediaPlayer.create(context, R.raw.gongsound)
    }

    private val soundBeforeStartTimer by lazy {
        MediaPlayer.create(context, R.raw.singletimersound)
    }

    actual fun startTrainingSound() {
        soundStartTraining.start()
    }

    actual fun timerBeforeStartSound() {
        soundBeforeStartTimer.start()
    }
}
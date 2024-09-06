package twine.presentation.detection

import android.graphics.Bitmap
import twine.presentation.data.Person

interface PoseDetector : AutoCloseable {

 fun estimatePoses(bitmap: Bitmap): List<Person>

 fun lastInferenceTimeNanos(): Long
}
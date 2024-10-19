package twine.presentation.detection

import android.graphics.Bitmap
import twine.presentation.data.Person
import twine.presentation.model.TypeTraining

interface PoseDetector : AutoCloseable {

 fun estimatePoses(bitmap: Bitmap, typeTraining: TypeTraining): List<Person>

 fun lastInferenceTimeNanos(): Long
}
package twine.presentation.ui

import android.graphics.Matrix

object CameraConstant {
   const val PREVIEW_WIDTH = 640
   const val PREVIEW_HEIGHT = 480

   const val ROTATION_0 = 0
   const val ROTATION_90 = 90
   const val ROTATION_180 = 180
   const val ROTATION_270 = 270

   val rotationMatrix90 = Matrix().apply {
      postRotate(90.0f)
   }

   val rotationMatrix270 = Matrix().apply {
      postRotate(270.0f)
   }

   val rotationMatrix0 = Matrix().apply {
      postRotate(0.0f)
   }

   val rotationMatrix180 = Matrix().apply {
      postRotate(180.0f)
   }
}
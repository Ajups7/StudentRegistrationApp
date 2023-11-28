package com.example.studentregistrationapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream

@Composable
fun TakePicture(navController: NavController, viewModel: StudentViewModel) {


    val applicationContext = LocalContext.current
    val activityContext = applicationContext as ComponentActivity
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {

                takePhoto(
                    controller = controller,
                    onPhotoTaken = {bitmap->
                                   viewModel.onTakePhoto(bitmap)
                        createImageFile(context = applicationContext, ){
                            val imageUrl = it.absolutePath
                            saveBitmapToFile(bytes = bitmap, filePath = imageUrl)
                            viewModel.onEvent(StudentEvent.SetImageUri(imageUrl))
                            viewModel.onEvent(StudentEvent.SaveStudentImage)

                        }
                    },
                    applicationContext = applicationContext,
                    navController = navController
                )
            }
        ){ Icon(
                imageVector = Icons.Default.PhotoCamera,
        contentDescription = "Take Photo"
        )
    }
    }
}
private fun takePhoto(controller: LifecycleCameraController,
                      applicationContext: Context,
                      onPhotoTaken: (Bitmap) -> Unit,
                      navController: NavController,) {
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
//                val matrix = Matrix().apply {
//                    postRotate(image.imageInfo.rotationDegrees.toFloat())
//                    postScale(-1f, 1f)
//                }
//
//                val rotatedBitmap = Bitmap.createBitmap(
//                    image.toBitmap(),
//                    0,
//                    0,
//                    image.width,
//                    image.height,
//                    matrix,
//                    true
//                )

                onPhotoTaken(image.toBitmap())


                navController.popBackStack()
            }
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "no photo", exception)
            }
        }

    )

}

private fun createImageFile(context: Context,onFileCreated:(File)->Unit) {
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val fileDir = File(context.filesDir, "images")
    if (!fileDir.exists()) fileDir.mkdir()
    val file = File(fileDir, fileName)
    onFileCreated(file)
}

fun saveBitmapToFile(bytes:Bitmap, filePath: String) {
    val outputStream = FileOutputStream(filePath)
    bytes.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
}

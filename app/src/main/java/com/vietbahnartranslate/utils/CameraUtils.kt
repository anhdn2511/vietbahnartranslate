package com.vietbahnartranslate.utils

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraUtils(private val onTextFound: (String) -> Unit) {
    private val TAG = this.javaClass.simpleName
    //private val imageAnalyzerExecutor : ExecutorService by lazy {Executors.newSingleThreadExecutor()}
    private val imageCaptureExecutor: ExecutorService by lazy {Executors.newSingleThreadExecutor()}

    private val textRecognizer = TextRecognizer(onTextFound)

//    private val imageAnalyzer by lazy {
//        ImageAnalysis.Builder()
//            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//            .build()
//            .also {
//                it.setAnalyzer(
//                    imageAnalyzerExecutor,
//                    ImageAnalyzer(onTextFound)
//                )
//            }
//    }

    private val imageCapture by lazy {
        ImageCapture.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }


    fun startCamera(context: Context, lifeCycleOwner: LifecycleOwner, surfaceProvider: SurfaceProvider) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val runnable = Runnable {
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(surfaceProvider) }
            with(cameraProviderFuture.get()) {
                unbindAll()
                bindToLifecycle(lifeCycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
            }
        }
        cameraProviderFuture.addListener(runnable, ContextCompat.getMainExecutor(context))
    }

    @ExperimentalGetImage
    fun captureImage() {
        imageCapture.takePicture(imageCaptureExecutor, object: ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val imageFromImageProxy = image.image ?: return
                textRecognizer.recognizeImageText(imageFromImageProxy, image.imageInfo.rotationDegrees) {
                    image.close()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.d(TAG, "Failed to capture image")
            }
        })
    }

    fun shutdown() {
        //imageAnalyzerExecutor.shutdown()
        imageCaptureExecutor.shutdown()
    }

}
package com.vietbahnartranslate.viewmodel.home

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TextRecognitionViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "TextRecognitionViewModel"

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var camera: Camera

    private lateinit var cameraExecutor: ExecutorService


    /**
     * LiveData
     */
    private val _textInImage = MutableLiveData(String())
    val textInImage : LiveData<String> = _textInImage

    private val _textInImageLayoutVisibility: MutableLiveData<Int> = MutableLiveData()
    val textInImageLayoutVisibility: LiveData<Int> = _textInImageLayoutVisibility

    /**
     * Method handle text recognition
     */
    fun init() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    fun runTextRecognition(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        textRecognizer
            .process(inputImage)
            .addOnSuccessListener { text ->
                // bind live data with text
                _textInImageLayoutVisibility.postValue(View.VISIBLE)
                processTextRecognitionResult(text)
            }.addOnFailureListener { e ->
                Log.d(TAG, "failed in text recognizer ${e.localizedMessage}")
            }
    }

    private fun processTextRecognitionResult(text: Text) {
        var finalText = ""
        for (block in text.textBlocks) {
            for (line in block.lines) {
                finalText += line.text + "\n"
            }
            finalText += "\n"
        }
        if (finalText.isNotEmpty()) {
            _textInImage.postValue(finalText)
        } else {
            _textInImage.postValue("Không tìm thấy chữ")
        }
    }

    fun startCamera(context: Context, lifecycleOwner: LifecycleOwner, surfaceProvider: SurfaceProvider) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)

            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed ${e.localizedMessage}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun shareText(text: String, context: Context) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share text"))
    }

     fun copyToClipboard(text: String, context: Context) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
    }

    fun shutdown() {
        cameraExecutor.shutdown()
    }
}
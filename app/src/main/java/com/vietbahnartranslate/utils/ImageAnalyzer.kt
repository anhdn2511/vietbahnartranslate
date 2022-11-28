package com.vietbahnartranslate.utils

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyzer(onTextFound: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val textRecognizer = TextRecognizer(onTextFound)

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val imageFromImageProxy = image.image ?: return
        textRecognizer.recognizeImageText(imageFromImageProxy, image.imageInfo.rotationDegrees) {
            image.close()
        }
    }
}
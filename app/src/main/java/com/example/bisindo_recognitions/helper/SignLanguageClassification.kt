package com.example.bisindo_recognitions.helper

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.lang.IllegalStateException

class SignLanguageClassification(
    private var threshold : Float = 0.5f,
    private var numThread: Int = 10,
    var maxResult : Int = 3,
    var context : Context,
    var classifierListener : ClassifierListener?
) {
    private var mainClassifier : ImageClassifier? = null

    init {
        setupClassifier()
    }

    private fun setupClassifier(){
        val optionBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThread)
        baseOptionsBuilder.useNnapi()
        optionBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            mainClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                "bisindo-meta.tflite",
                optionBuilder.build()
            )
        }catch (e : IllegalStateException){
            classifierListener?.error(e.toString())
            Log.d("helper",e.toString())
        }
    }

    fun recognition(image : Bitmap,rotation : Int){
        if (mainClassifier == null){
            setupClassifier()
        }
        var inferenceTime = SystemClock.uptimeMillis()
        val imageProcessor = ImageProcessor.Builder()
//            .add(Rot90Op(rotation / 90))
            .build()
        val tensorImage = imageProcessor.process(
            TensorImage.fromBitmap(image)
        )
        val result = mainClassifier?.classify(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListener?.result(result,inferenceTime)
    }

    interface ClassifierListener{
        fun error(e : String)
        fun result(
            result: List<Classifications>?,
            interferenceTime : Long
        )
    }

}
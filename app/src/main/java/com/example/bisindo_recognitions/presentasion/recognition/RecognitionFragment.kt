package com.example.bisindo_recognitions.presentasion.recognition

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bisindo_recognitions.databinding.FragmentRecognitionBinding
import com.example.bisindo_recognitions.helper.SignLanguageClassification
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecognitionFragment : Fragment(), SignLanguageClassification.ClassifierListener {
    private lateinit var binding : FragmentRecognitionBinding
    private var cameraProvider : ProcessCameraProvider? = null
    private var preview : Preview? = null
    private var imageAnalyzer : ImageAnalysis? = null
    private var camera : Camera? = null
    private lateinit var cameraExecutor : ExecutorService
    private lateinit var bitmapBuffer : Bitmap

    private var cameraSelector : CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private lateinit var signLanguageClassification: SignLanguageClassification

    private val resultAdapter by lazy {
        ResultRecyclerViewAdapter().apply {
            updateAdapterSize(signLanguageClassification.maxResult)
        }
    }

    private fun allPermissionGranted() = required_permission.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("request",requestCode.toString())
        if(requestCode == REQUEST_CODE_PERMISSION){
            if (!allPermissionGranted()){
                Toast.makeText(
                    requireContext(),
                    "no permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecognitionBinding.inflate(layoutInflater)
        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                requireActivity(),
                required_permission,
                REQUEST_CODE_PERMISSION
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signLanguageClassification = SignLanguageClassification(
            context = requireContext(),
            classifierListener = this
        )
        binding.recview.layoutManager = LinearLayoutManager(requireContext())
        binding.recview.adapter = resultAdapter

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnRotate.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
                CameraSelector.DEFAULT_BACK_CAMERA
            else
                CameraSelector.DEFAULT_FRONT_CAMERA
            setUpCamera()
        }

        binding.cameraView.post {
            setUpCamera()
        }

    }


    private fun setUpCamera(){
        val cameraProviderListener = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderListener.addListener({
            cameraProvider = cameraProviderListener.get()
            initCameraUseCases()
        },ContextCompat.getMainExecutor(requireContext()))
    }

    private fun initCameraUseCases(){
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.cameraView.display.rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.cameraView.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor){ image ->
                    if(!::bitmapBuffer.isInitialized){
                        bitmapBuffer = Bitmap.createBitmap(
                            image.width,
                            image.height,
                            Bitmap.Config.ARGB_8888
                        )
                    }
                    recognitionCamera(image)
                }
            }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                binding.cameraView.surfaceProvider
            )
        }catch (e : Exception){
            Log.d("recognition_fragment",e.message.toString())
        }
    }

    private fun recognitionCamera(image : ImageProxy){
        image.use {
            bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)
        }

        val flipMatrix = Matrix().apply {
            postScale(
                -1f,
                1f,
                bitmapBuffer.width / 2f,
                bitmapBuffer.height / 2f
            )
        }
        val flippedBitmap = Bitmap.createBitmap(
            bitmapBuffer, 0, 0,
            bitmapBuffer.width, bitmapBuffer.height, flipMatrix, true
        )
        val imageRotation = 180 - image.imageInfo.rotationDegrees
        signLanguageClassification.recognition(
            flippedBitmap,imageRotation
        )
    }

    override fun error(e: String) {
        activity?.runOnUiThread {
            resultAdapter.updateResults(null)
            resultAdapter.notifyDataSetChanged()
        }
    }

    override fun result(result: List<Classifications>?, interferenceTime: Long) {
        activity?.runOnUiThread {
            resultAdapter.updateResults(result)
            resultAdapter.notifyDataSetChanged()
        }
    }

    companion object{
        private val required_permission = arrayOf(
            android.Manifest.permission.CAMERA
        )
        private const val REQUEST_CODE_PERMISSION = 10
    }

}
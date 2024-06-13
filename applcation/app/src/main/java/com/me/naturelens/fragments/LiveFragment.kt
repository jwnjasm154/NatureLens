package com.me.naturelens.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.me.naturelens.InfoPlantActivity
import com.me.naturelens.InitApp
import com.me.naturelens.MainActivity
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant
import com.me.naturelens.utils.ImageClassifierHelper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LiveFragment : Fragment(), ImageClassifierHelper.ClassifierListener {
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onResume() {
        super.onResume()

        backgroundExecutor.execute {
            if (imageClassifierHelper.isClosed()) {
                imageClassifierHelper.setupImageClassifier()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (camera?.cameraInfo?.torchState?.value != 0) {
            camera?.cameraControl?.enableTorch(false)
                }
        // Close the image classifier and release resources
        backgroundExecutor.execute { imageClassifierHelper.clearImageClassifier() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (camera?.cameraInfo?.torchState?.value != 0) {
            camera?.cameraControl?.enableTorch(false)
}
        // Shut down our background executor
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  v =inflater.inflate(R.layout.fragment_live, container, false)
        v.findViewById<Button>(R.id.flashopen).setOnClickListener {
            camera?.let { it1 -> setFlashIcon(it1,v.context) }
        }
        backgroundExecutor = Executors.newSingleThreadExecutor()
        backgroundExecutor.execute {
            imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                threshold = 0.3f,
                maxResults = 3,
                imageClassifierListener = this
            )

                v.findViewById<PreviewView>(R.id.view_finder)?.post {
                // Set up the camera and its use cases
                    if (allPermissionsGranted()) {
                        setUpCamera()
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                    }

            }
        }
        return v

    }
    private fun setFlashIcon(camera: Camera,context: Context) {
        if (camera.cameraInfo.hasFlashUnit()) {
            if (camera.cameraInfo.torchState.value == 0) {
                camera.cameraControl.enableTorch(true)
 } else {
                camera.cameraControl.enableTorch(false)
}
        } else {
            Toast.makeText(
                context,
                "Flash is Not Available",
                Toast.LENGTH_LONG
            ).show()
}
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setUpCamera()
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(requireContext())
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            view?.findViewById<PreviewView>(R.id.view_finder)?.display?.rotation!!
    }
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            view?.findViewById<PreviewView>(R.id.view_finder)?.display?.let {
                Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(it.rotation)
                    .build()
            }

        imageAnalyzer =
            view?.findViewById<PreviewView>(R.id.view_finder)?.display?.let {
                ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(it.rotation)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                    // The analyzer can then be assigned to the instance
                    .also {
                        it.setAnalyzer(
                            backgroundExecutor,
                            imageClassifierHelper::classifyLiveStreamFrame
                        )
                    }
            }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )
            view?.let { context?.let { it1 -> setUpZo1omTapToFocus(it, it1) } }
            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(view?.findViewById<PreviewView>(R.id.view_finder)?.surfaceProvider)
        } catch (exc: Exception) {
            Log.e("{Error}", "Use case binding failed", exc)
        }
    }
    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
        }

    }
    private fun setUpZo1omTapToFocus(view1: View,context1: Context){
 val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener(){
     override fun onScale(detector: ScaleGestureDetector): Boolean {
         val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
         val delta = detector.scaleFactor
         camera?.cameraControl?.setZoomRatio(currentZoomRatio*delta)
         return true
     }
 }
        val scaleGestureDetector = ScaleGestureDetector(context1,listener)
        view1.findViewById<PreviewView>(R.id.view_finder).setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)
            //Focus
            if (event.action == MotionEvent.ACTION_DOWN){
                val factory = view1.findViewById<PreviewView>(R.id.view_finder).meteringPointFactory
                val point = factory.createPoint(event.x,event.y)
                val action = FocusMeteringAction.Builder(point,FocusMeteringAction.FLAG_AF)
                    .setAutoCancelDuration(2,TimeUnit.SECONDS)
                    .build()

                camera?.cameraControl?.startFocusAndMetering(action)

                view.performClick()
            }
            true
        }
    }
    fun setFragmentFormFragment(context: Context ,plantName:String) {
        val plantlist =  (context.applicationContext as InitApp).plantlist
        val filteredList:List<ModelPlant> =
            plantlist.filter { item ->
                item.name.en.equals(plantName.trimEnd('\r'),ignoreCase = true)
            }
        if(filteredList.isNotEmpty() && filteredList.size < 2){
            Log.i("testScann",filteredList.toString())
            val plant = filteredList.first()
            if (context.resources.configuration.locale.language.equals("en")) {
                val intent = Intent(context, InfoPlantActivity::class.java)
                intent.putExtra("id",plant.id)
                intent.putExtra("name", plant.name.en)
                intent.putExtra("kind", plant.kind.en)
                intent.putExtra("desc", plant.desc.en)
                intent.putExtra("tags", plant.tags.en.joinToString(", "))
                intent.putExtra("image", plant.mainImage)
                intent.putExtra("other_images", plant.otherImage.toIntArray())

                context.startActivity(intent)
            }
            else {
                val intent = Intent(context, InfoPlantActivity::class.java)
                intent.putExtra("id", plant.id)
                intent.putExtra("name", plant.name.ar)
                intent.putExtra("kind", plant.kind.ar)
                intent.putExtra("desc", plant.desc.ar)
                intent.putExtra("tags", plant.tags.ar.joinToString(", "))
                intent.putExtra("image", plant.mainImage)
                intent.putExtra("other_images", plant.otherImage.toIntArray())

                context.startActivity(intent)
            }
        }else {
            Log.i("testScann",filteredList.toString())
}

    }
    override fun onResults(resultBundle: ImageClassifierHelper.ResultBundle) {
        activity?.runOnUiThread {
            view?.let {
                val r =  resultBundle.results.first().classificationResult().classifications()[0].categories().sortedBy { it.index() }
                when (r.size){
                1 -> {
                    it.findViewById<TextView>(R.id.liveResult).text = r.first().categoryName()
                    it.findViewById<TextView>(R.id.liveResult).setOnClickListener { setFragmentFormFragment(it.context,r.first().categoryName()) }
                    it.findViewById<TextView>(R.id.liveResult1).text = "-"
                    it.findViewById<TextView>(R.id.liveResult2).text = "-"

                }
            2->{
                it.findViewById<TextView>(R.id.liveResult).text = r.first().categoryName()
                it.findViewById<TextView>(R.id.liveResult).setOnClickListener { setFragmentFormFragment(it.context,r.first().categoryName()) }

                it.findViewById<TextView>(R.id.liveResult1).text =  r[1].categoryName()
                it.findViewById<TextView>(R.id.liveResult1).setOnClickListener { setFragmentFormFragment(it.context,r[1].categoryName()) }

                it.findViewById<TextView>(R.id.liveResult2).text = "-"
                }
                    0->{
                    it.findViewById<TextView>(R.id.liveResult).text = "Not Found"
                    it.findViewById<TextView>(R.id.liveResult1).text = "-"
                    it.findViewById<TextView>(R.id.liveResult2).text = "-"
                    }

                    else -> {
                        it.findViewById<TextView>(R.id.liveResult).text = r.first().categoryName()
                        it.findViewById<TextView>(R.id.liveResult).setOnClickListener { setFragmentFormFragment(it.context,r.first().categoryName()) }

                        it.findViewById<TextView>(R.id.liveResult1).text = r[1].categoryName()
                        it.findViewById<TextView>(R.id.liveResult1).setOnClickListener { setFragmentFormFragment(it.context,r[1].categoryName()) }

                        it.findViewById<TextView>(R.id.liveResult2).text = r[2].categoryName()
                        it.findViewById<TextView>(R.id.liveResult2).setOnClickListener { setFragmentFormFragment(it.context,r[2].categoryName()) }

                    }
                }
            }
        }



    }
    companion object {
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
package com.example.financyq.ui.photo

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.financyq.databinding.ActivityPhotoBinding
import com.example.financyq.getImageUri
import com.example.financyq.helper.ImageClassifierHelper
import com.example.financyq.reduceFileImage
import com.example.financyq.ui.result.ResultActivity
import com.example.financyq.uriToFile
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class PhotoActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: ActivityPhotoBinding
    private var currentImageUri: Uri? = null
    private var currentLabel: String? = null
    private var currentConfidenceScore: String? = null
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(this.cacheDir, "cropped${System.currentTimeMillis()}.jpg")
        )
        val uCropOptions = UCrop.Options()
        uCropOptions.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        UCrop.of(sourceUri, destinationUri)
            .withOptions(uCropOptions)
            .start(this)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startUCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { startUCrop(it) }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(currentImageUri)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri == null){
            showToast("Image didn't exist")
            return
        }

        ImageClassifierHelper(
            context = this,
            classifierListener = this
        ).classifyStaticImage(currentImageUri!!)

    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, currentLabel.toString())
        intent.putExtra(ResultActivity.EXTRA_SCORE, currentConfidenceScore.toString())
        //tambahin putExtra ke manual activity untuk
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        showToast(error)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        generateResult(results)
    }

    private fun generateResult(data: List<Classifications>?) {
        data?.let { it ->
            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                Log.e("aisyah", "generateResult: $it", )
                Log.e("aisyah2", "generateResult: ${it[0]}", )
                val highestResult =
                    it[0].categories.maxBy {
                        it?.score ?: 0.0f
                    }

                currentLabel = highestResult.label
                currentConfidenceScore = NumberFormat.getPercentInstance()
                    .format(highestResult.score)
                moveToResult()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)?.message.toString()
            showToast(cropError)
            Log.e(ContentValues.TAG, cropError)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
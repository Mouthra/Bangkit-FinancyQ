package com.example.financyq.ui.photo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.R
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.databinding.ActivityPhotoBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import com.example.financyq.data.di.Result

class PhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoBinding
    private var currentImageUri: Uri? = null
    private val postImageViewModel: PostImageViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnAnalyze.setOnClickListener { uploadImage() }
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            cropImage(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let {
                cropImage(it)
            }
        }
    }

    private fun cropImage(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                currentImageUri = result.uri
                showImage()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e("Crop Error", "Crop error: ", error)
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            postImageViewModel.postImage(multipartBody).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        binding.pbAnalize.visibility = View.GONE
                        handleSuccess(result.data, imageFile)
                    }

                    is Result.Loading -> {
                        binding.pbAnalize.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.pbAnalize.visibility = View.GONE
                        handleError(result.error)
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun handleSuccess(responseBody: ResponseBody, imageFile: File) {
        try {
            val responseString = responseBody.string()
            val jsonObject = JSONObject(responseString)

            val item = jsonObject.getString("item")
            val totalPrice = jsonObject.getString("totalPrice")

            val intent = Intent(this, ResultsActivity::class.java).apply {
                putExtra("item", item)
                putExtra("totalPrice", totalPrice)
                putExtra("imageUri", Uri.fromFile(imageFile).toString())
            }
            startActivity(intent)

        } catch (e: JSONException) {
            Log.e("JSON Parse Error", "Error parsing JSON: ${e.message}")
            showToast("Error parsing JSON response")
        }
    }

    private fun handleError(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

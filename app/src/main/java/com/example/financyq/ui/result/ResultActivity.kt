package com.example.financyq.ui.result

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financyq.R
import com.example.financyq.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityResultBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_result)

        val score = intent.getStringExtra(EXTRA_SCORE)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val imageUri = Uri.parse(image)
        binding.resultText.text = "$score $prediction"
        binding.resultImage.setImageURI(imageUri)
    }
    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_PREDICTION = "extra_prediction"
        const val EXTRA_SCORE = "extra_score"
    }
}
package com.example.financyq.ui.about

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financyq.R
import com.example.financyq.databinding.ActivityAboutFinancyQBinding

class AboutFinancyQActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAboutFinancyQBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutFinancyQBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
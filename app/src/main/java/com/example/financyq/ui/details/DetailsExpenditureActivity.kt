package com.example.financyq.ui.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financyq.R
import com.example.financyq.databinding.ActivityDetailsExpenditureBinding
import com.example.financyq.databinding.ActivityManualBinding

class DetailsExpenditureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsExpenditureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsExpenditureBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.financyq

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.databinding.ActivitySignupBinding
import com.example.financyq.ui.otp.OtpActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()

    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, OtpActivity::class.java))
        }
    }
}
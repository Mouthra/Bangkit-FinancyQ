package com.example.financyq.ui.otp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.request.OtpRequest
import com.example.financyq.data.request.SignupRequest
import com.example.financyq.databinding.ActivityOtpBinding
import com.example.financyq.ui.login.LoginActivity

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private val otpViewModel by viewModels<OtpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var email = ""
    private var password = ""
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
        setupActionResendOtp()
    }

    private fun setupActionResendOtp() {
        binding.tvResend.setOnClickListener {
            resendSignup()
        }
    }

    private fun resendSignup() {
        email = intent.getStringExtra(EXTRA_EMAIL).toString()
        password = intent.getStringExtra(EXTRA_PASSWORD).toString()
        username = intent.getStringExtra(EXTRA_USERNAME).toString()

        val signupRequest = SignupRequest(username, email, password)
        otpViewModel.resendSignup(signupRequest).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Resend Success")
                        setMessage("OTP has been resent successfully.")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
                is Result.Loading -> {
                    // Show loading indicator if needed
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAction() {
        email = intent.getStringExtra(EXTRA_EMAIL).toString()
        binding.tvDescEmail.text = email
        binding.btnVerify.setOnClickListener {
            val otp = binding.OtpEditText.text.toString()

            if (otp.isEmpty()) {
                Toast.makeText(this, "OTP is empty", Toast.LENGTH_SHORT).show()
            } else {
                val otpRequest = OtpRequest(email = email, otp = otp)
                otpViewModel.verifyOtp(otpRequest).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Verification successful")
                                setMessage("OTP verification was successful.")
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
                                    finish()
                                }
                                setCancelable(false)
                                create()
                                show()
                            }
                        }
                        is Result.Loading -> {
                            // Show loading indicator if needed
                        }
                        is Result.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
    }
}

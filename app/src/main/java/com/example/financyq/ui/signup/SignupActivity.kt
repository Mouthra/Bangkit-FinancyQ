package com.example.financyq.ui.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.R
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.databinding.ActivitySignupBinding
import com.example.financyq.data.di.Result
import com.example.financyq.data.request.SignupRequest
import com.example.financyq.ui.otp.OtpActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Data Still Empty", Toast.LENGTH_SHORT).show()
            } else {
                val signupRequest = SignupRequest(username, email, password)
                signUpViewModel.register(signupRequest).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            // Show a loading indicator if needed
                        }

                        is Result.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.title_set)
                                setMessage(R.string.message_set)
                                setPositiveButton(R.string.continue_set) { _, _ ->
                                    val intent = Intent(context, OtpActivity::class.java).apply {
                                        putExtra(OtpActivity.EXTRA_EMAIL, email)
                                        putExtra(OtpActivity.EXTRA_USERNAME, username)
                                        putExtra(OtpActivity.EXTRA_PASSWORD, password)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                                setCancelable(false)
                                create()
                                show()
                            }
                        }

                        is Result.Error -> {
                            val errorMessage = result.error
                            when {
                                errorMessage.contains("Username or email already exists") -> {
                                    Toast.makeText(this, R.string.username_or_email_already_exists, Toast.LENGTH_SHORT).show()
                                }
                                errorMessage.contains("Invalid email format") -> {
                                    Toast.makeText(this, R.string.invalid_email_format, Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

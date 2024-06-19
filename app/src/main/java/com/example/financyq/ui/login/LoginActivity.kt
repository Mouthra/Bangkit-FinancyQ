// LoginActivity.kt
package com.example.financyq.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.MainActivity
import com.example.financyq.R
import com.example.financyq.data.request.LoginRequest
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.email_or_password_cannot_be_empty, Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(email, password)
                loginViewModel.login(loginRequest).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {

                        }

                        is Result.Success -> {
                            showCongratulationsDialog()
                        }

                        is Result.Error -> {
                            val errorMessage = result.error
                            when {
                                errorMessage.contains("Invalid password") -> {
                                    Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                                }
                                errorMessage.contains("User not found") -> {
                                    Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show()
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

    private fun showCongratulationsDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.congratulations)
            setMessage(R.string.you_have_successfully_login)
            setPositiveButton(R.string.continue_value) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            setCancelable(false)
            create()
            show()
        }
    }
}

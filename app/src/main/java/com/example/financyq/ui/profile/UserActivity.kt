package com.example.financyq.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.databinding.ActivityUserBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val usernameViewModel: UsernameViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences.getInstance(this)

        setupAction()
        loadUser()
    }

    private fun loadUser() {
        val username = runBlocking { userPreferences.userNameFlow.first() }
        if (username != null) {
            usernameViewModel.getUsername(username).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        // Show loading indicator if needed
                    }

                    is Result.Success -> {
                        binding.nameEditText.setText(result.data.username)
                        binding.emailEditText.setText(result.data.email)
                        binding.passwordEditText.setText(result.data.password)
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}

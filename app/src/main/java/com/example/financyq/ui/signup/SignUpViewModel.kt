package com.example.financyq.ui.signup

import androidx.lifecycle.ViewModel
import com.example.financyq.data.repo.UserRepository
import com.example.financyq.data.request.SignupRequest

class SignUpViewModel(private val userRepository: UserRepository): ViewModel() {

//    fun signup(username: String, email: String, password: String) =userRepository.signup(username, email, password)

    fun register(signupRequest: SignupRequest) =userRepository.register(signupRequest)

}
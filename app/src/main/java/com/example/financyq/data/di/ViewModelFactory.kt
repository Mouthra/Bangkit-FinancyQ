package com.example.financyq.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financyq.data.repo.EduFinanceRepository
import com.example.financyq.data.repo.UserRepository
import com.example.financyq.ui.edufinance.EduFinanceViewModel
import com.example.financyq.ui.otp.OtpViewModel
import com.example.financyq.ui.signup.SignUpViewModel

class ViewModelFactory(
    private val eduFinanceRepository: EduFinanceRepository,
    private val userRepository: UserRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EduFinanceViewModel::class.java) -> {
                EduFinanceViewModel(eduFinanceRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                OtpViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory {
            val eduFinanceRepository = Injection.provideEduFinanceRepository()
            val userRepository = Injection.provideUserRepository()
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(eduFinanceRepository, userRepository)
                    .also { INSTANCE = it }
            }
        }
    }
}
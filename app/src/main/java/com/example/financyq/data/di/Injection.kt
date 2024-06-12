package com.example.financyq.data.di

import android.content.Context
import com.example.financyq.data.api.ApiConfig
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.repo.EduFinanceRepository
import com.example.financyq.data.repo.UserRepository

object Injection {
    fun provideEduFinanceRepository(context: Context): EduFinanceRepository{
        val userPreferences =UserPreferences.getInstance(context)
        val tokenFlow = userPreferences.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return EduFinanceRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository{
        val userPreferences =UserPreferences.getInstance(context)
        val tokenFlow = userPreferences.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return UserRepository.getInstance(apiService, userPreferences)
    }
}
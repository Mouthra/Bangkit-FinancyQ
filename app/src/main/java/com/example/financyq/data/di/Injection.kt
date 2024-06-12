package com.example.financyq.data.di

import android.content.Context
import com.example.financyq.data.api.ApiConfig
import com.example.financyq.data.repo.EduFinanceRepository
import com.example.financyq.data.repo.UserRepository

object Injection {
    fun provideEduFinanceRepository(): EduFinanceRepository{
        val apiService = ApiConfig.getApiService()
        return EduFinanceRepository.getInstance(apiService)
    }

    fun provideUserRepository(): UserRepository{
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}
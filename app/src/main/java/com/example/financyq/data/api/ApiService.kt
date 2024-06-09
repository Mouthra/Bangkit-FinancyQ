package com.example.financyq.data.api

import com.example.financyq.data.response.EduFinanceResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("GETAll-Edu")
    suspend fun getEducationFinance(
        @Header("Authorization") token: String
    ): List<EduFinanceResponse>

}
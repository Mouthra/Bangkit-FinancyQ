package com.example.financyq.data.api

import com.example.financyq.data.request.OtpRequest
import com.example.financyq.data.request.SignupRequest
import com.example.financyq.data.response.EduFinanceResponse
import com.example.financyq.data.response.OtpResponse
import com.example.financyq.data.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("education-content")
    suspend fun getEducationFinance(
    ): Response<List<EduFinanceResponse>>

//    @FormUrlEncoded
//    @POST("signup")
//    suspend fun signup(
//        @Field("username") username: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Response<SignUpResponse>


    @POST("signup")
    suspend fun register(
        @Body signupRequest : SignupRequest
    ): Response<SignUpResponse>

    @POST("verifyOtp")
    suspend fun verifyOtp(
        @Body otpRequest: OtpRequest
    ): Response<OtpResponse>
}

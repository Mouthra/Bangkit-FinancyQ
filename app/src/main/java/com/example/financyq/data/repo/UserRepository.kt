package com.example.financyq.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.financyq.data.api.ApiService
import com.example.financyq.data.response.SignUpResponse
import com.example.financyq.data.di.Result
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.request.LoginRequest
import com.example.financyq.data.request.OtpRequest
import com.example.financyq.data.request.SignupRequest
import com.example.financyq.data.response.LoginResponse
import com.example.financyq.data.response.OtpResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, private val userPreferences: UserPreferences) {
//    fun signup(username: String, email: String, password: String): LiveData<Result<SignUpResponse>> =
//        liveData {
//            emit(Result.Loading)
//            try {
//                val response = apiService.signup(username, email, password)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        if (responseBody.success == true) {
//                            emit(Result.Success(responseBody))
//                        } else {
//                            emit(Result.Error(responseBody.message ?: "Unknown error"))
//                        }
//                    } else {
//                        emit(Result.Error("Response body is null"))
//                    }
//                } else {
//                    emit(Result.Error("Unsuccessful response"))
//                }
//            } catch (e: HttpException) {
//                val errorBody = e.response()?.errorBody()?.string()
//                val errorResponse = Gson().fromJson(errorBody, SignUpResponse::class.java)
//                emit(Result.Error(errorResponse.message ?: "Uknown Error"))
//            } catch (e: Exception) {
//                emit(Result.Error(e.message ?: "Expected occurred"))
//            }
//        }

    fun register(signupRequest: SignupRequest): LiveData<Result<SignUpResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(signupRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.success == true) {
                            emit(Result.Success(responseBody))
                        } else {
                            emit(Result.Error(responseBody.message ?: "Unknown error"))
                        }
                    } else {
                        emit(Result.Error("Response body is null"))
                    }
                } else {
                    emit(Result.Error("Unsuccessful response"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, SignUpResponse::class.java)
                emit(Result.Error(errorResponse.message ?: "Uknown Error"))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Expected occurred"))
            }
        }

    fun verifyOtp(otpRequest: OtpRequest): LiveData<Result<OtpResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.verifyOtp(otpRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.success == true) {
                            emit(Result.Success(responseBody))
                        } else {
                            emit(Result.Error(responseBody.message ?: "Unknown error"))
                        }
                    } else {
                        emit(Result.Error("Response body is null"))
                    }
                } else {
                    emit(Result.Error("Unsuccessful response"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, OtpResponse::class.java)
                emit(Result.Error(errorResponse.message ?: "Unknown Error"))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unexpected error occurred"))
            }
        }

//    fun login(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> =
//        liveData {
//            emit(Result.Loading)
//            try {
//                val response = apiService.login(loginRequest)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        if (responseBody.error == true) {
//                            emit(kotlin.Result.Error(responseBody.message ?: "Unknown error"))
//                        } else {
//                            emit(kotlin.Result.Success(responseBody))
//                            responseBody.loginResult?.refreshToken?.let { token ->
//                                userPreferences.saveToken(token)
//                                Log.d("Login", "Token saved: $token")
//                            }
//                        }
//                    } else {
//                        emit(Result.Error("Response body is null"))
//                    }
//                } else {
//                    emit(Result.Error(response.message() ?: "Unknown error"))
//                }
//            } catch (e: HttpException) {
//                val errorBody = e.response()?.errorBody()?.string()
//                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
//                emit(Result.Error(errorResponse.message ?: "Unknown error"))
//            } catch (e: Exception) {
//                emit(Result.Error(e.message ?: "Exception occurred"))
//            }
//        }

    fun login(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    responseBody.refreshToken?.let { token ->
                        withContext(Dispatchers.IO) {
                            userPreferences.saveToken(token)
                        }
                    }
                    emit(Result.Success(responseBody))
                } else {
                    emit(Result.Error("Response body is null"))
                }
            } else {
                emit(Result.Error("Unsuccessful response"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse?.refreshToken ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Exception occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService , userPreferences)
            }.also { instance = it }
    }
}

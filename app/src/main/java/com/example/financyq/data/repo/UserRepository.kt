package com.example.financyq.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.financyq.data.api.ApiService
import com.example.financyq.data.response.SignUpResponse
import com.example.financyq.data.di.Result
import com.example.financyq.data.request.OtpRequest
import com.example.financyq.data.request.SignupRequest
import com.example.financyq.data.response.OtpResponse
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService) {
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

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}

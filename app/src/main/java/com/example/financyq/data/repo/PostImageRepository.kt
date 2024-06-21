package com.example.financyq.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.financyq.data.api.ApiService
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import com.example.financyq.data.di.Result

class PostImageRepository(private val apiService: ApiService) {
    fun postImage(image: MultipartBody.Part): LiveData<Result<ResponseBody>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.postImage(image)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Result.Success(responseBody))
                } else {
                    emit(Result.Error("Response body is null"))
                }
            } else {
                emit(Result.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Exception occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: PostImageRepository? = null

        fun getInstance(apiService: ApiService): PostImageRepository =
            instance ?: synchronized(this) {
                instance ?: PostImageRepository(apiService)
            }.also { instance = it }
    }
}

package com.example.financyq.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.financyq.data.repo.PostImageRepository
import com.example.financyq.data.di.Result
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class PostImageViewModel(private val repository: PostImageRepository) : ViewModel() {

    fun postImage(
        image: MultipartBody.Part
    ): LiveData<Result<ResponseBody>> {
        return repository.postImage(image)
    }
}

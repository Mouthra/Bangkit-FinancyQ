package com.example.financyq.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)

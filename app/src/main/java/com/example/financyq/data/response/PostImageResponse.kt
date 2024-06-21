package com.example.financyq.data.response

import com.google.gson.annotations.SerializedName

data class PostImageResponse(

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

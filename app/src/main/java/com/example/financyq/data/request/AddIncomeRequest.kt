package com.example.financyq.data.request

import com.google.gson.annotations.SerializedName

data class AddIncomeRequest(
    @field:SerializedName("idUser")
    val idUser: String? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("sumber")
    val sumber: String? = null,

    @field:SerializedName("kategori")
    val kategori: String? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

)

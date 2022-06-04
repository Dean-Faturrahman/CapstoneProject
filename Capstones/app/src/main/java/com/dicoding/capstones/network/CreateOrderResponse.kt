package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)
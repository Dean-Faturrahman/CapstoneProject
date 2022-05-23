package com.dicoding.capstones.data

import com.google.gson.annotations.SerializedName

data class UserLoginModel(
    @SerializedName("email")
    val userEmail: String?,

    @SerializedName("password")
    val userPassword: String?,
)
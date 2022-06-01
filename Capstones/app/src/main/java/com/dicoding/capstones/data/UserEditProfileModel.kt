package com.dicoding.capstones.data

import com.google.gson.annotations.SerializedName

data class UserEditProfileModel (

     @SerializedName("userid")
     val userId: String?,

     @SerializedName("photo")
     val photo: String?,

     @SerializedName("password")
     val userPassword: String?,

     @SerializedName("name")
     val userName: String?,

     @SerializedName("phone")
     val userPhone: String?,

     @SerializedName("dob")
     val userDob: String?,

     @SerializedName("gender")
     val userGender: String?,
 )
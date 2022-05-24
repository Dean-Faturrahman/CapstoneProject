package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: List<ProfileDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class ProfileDataItem(

	@field:SerializedName("User_Email")
	val userEmail: String,

	@field:SerializedName("User_Name")
	val userName: String,

	@field:SerializedName("User_Role")
	val userRole: String,

	@field:SerializedName("User_Phone")
	val userPhone: String,

	@field:SerializedName("User_Gender")
	val userGender: String,

	@field:SerializedName("User_Id")
	val userId: String,

	@field:SerializedName("User_Photo")
	val userPhoto: String,

	@field:SerializedName("User_DOB")
	val userDOB: String
)

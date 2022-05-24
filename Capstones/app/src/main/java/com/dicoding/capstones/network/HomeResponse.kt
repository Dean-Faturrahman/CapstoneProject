package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("data")
	val data: List<HomeDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class HomeDataItem(

	@field:SerializedName("User_Name")
	val userName: String,

	@field:SerializedName("User_Role")
	val userRole: String,

	@field:SerializedName("User_Id")
	val userId: String,

	@field:SerializedName("User_Photo")
	val userPhoto: String
)

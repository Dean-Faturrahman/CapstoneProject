package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class SuggestionsResponse(

	@field:SerializedName("data")
	val data: List<SearchDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class SuggestionsItem(

	@field:SerializedName("Class_Id")
	val classId: String,

	@field:SerializedName("User_Name")
	val userName: String,

	@field:SerializedName("User_Gender")
	val userGender: String,

	@field:SerializedName("User_Photo")
	val userPhoto: String,

	@field:SerializedName("Subject_Name")
	val subjectName: String
)

package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class ClassResponse(

	@field:SerializedName("data")
	val data: List<ClassList>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class ClassList(

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

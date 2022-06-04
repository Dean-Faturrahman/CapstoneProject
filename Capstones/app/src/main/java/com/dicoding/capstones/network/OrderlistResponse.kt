package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class OrderlistResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItem(

	@field:SerializedName("Order_Id")
	val orderId: String,

	@field:SerializedName("Order_Status")
	val orderStatus: String,

	@field:SerializedName("Class_Id")
	val classId: String,

	@field:SerializedName("Teacher_Id")
	val teacherId: String
)

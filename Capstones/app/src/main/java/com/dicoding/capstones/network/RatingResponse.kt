package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class RatingResponse(

	@field:SerializedName("data")
	val data: List<RatingDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class RatingDataItem(

	@field:SerializedName("User_Photo")
	val userPhoto: String,

	@field:SerializedName("User_Name")
		val userName: String,

	@field:SerializedName("Order_Rating")
	val orderRating: String,

	@field:SerializedName("Order_RatingDesc")
	val orderRatingDesc: String
)

package com.dicoding.capstones.network

import com.google.gson.annotations.SerializedName

data class PredictionsResponse(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem>
)

data class PredictionsItem(

	@field:SerializedName("output_2")
	val output2: List<String>,

	@field:SerializedName("output_1")
	val output1: List<Double>
)

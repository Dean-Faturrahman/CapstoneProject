package com.dicoding.capstones.data

import com.google.gson.annotations.SerializedName

data class UpgradeModel (
    @SerializedName("userid")
    val userId: String?,

    @SerializedName("idcard")
    val idCard: String?,

    @SerializedName("teachexp")
    val teachExp: Int?,

    @SerializedName("subject")
    val subject: String?,

     @SerializedName("score")
     val score: Int?,
        )
package com.dicoding.capstones.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("email") userEmail: String,
        @Field("password") userPassword: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("signup.php")
    fun signUpUser(
        @Field("email") userEmail: String,
        @Field("password") userPassword: String,
        @Field("name") userName: String,
        @Field("phone") userPhone: String,
        @Field("dob") userDob: String,
        @Field("gender") userGender: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("home.php")
    fun homeUser(
        @Field("email") userEmail: String?
    ): Call<HomeResponse>

    @FormUrlEncoded
    @POST("class.php")
    fun getClass(
        @Field("subject_name") subjectName: String?
    ): Call<ClassResponse>

    @FormUrlEncoded
    @POST("profile.php")
    fun profile(
        @Field("userid") userId: String?
    ): Call<ProfileResponse>
}
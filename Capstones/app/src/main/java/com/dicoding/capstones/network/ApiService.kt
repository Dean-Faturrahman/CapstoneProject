package com.dicoding.capstones.network

import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("search.php")
    fun getUserList(
        @Field("search") search: String?
    ): Call<SearchResponse>

    @FormUrlEncoded
    @POST("editprofile.php")
    fun editProfileWithPass(
        @Field("userid") userId: String,
        @Field("photo") photo: String,
        @Field("password") userPassword: String,
        @Field("name") userName: String,
        @Field("phone") userPhone: String,
        @Field("dob") userDob: String,
        @Field("gender") userGender: String,
    ): Call<EditProfileResponse>

    @FormUrlEncoded
    @POST("editprofile.php")
    fun editProfile(
        @Field("userid") userId: String,
        @Field("photo") photo: String,
        @Field("name") userName: String,
        @Field("phone") userPhone: String,
        @Field("dob") userDob: String,
        @Field("gender") userGender: String,
    ): Call<EditProfileResponse>

    @FormUrlEncoded
    @POST("verif.php")
    fun upgradeUser(
        @Field("userid") userId: String?,
        @Field("idcard") idCard: String,
        @Field("teachexp") teachExp: Int,
        @Field("subject") subject: String,
        @Field("score") score: Int,
    ): Call<UpgradeUserResponse>
}
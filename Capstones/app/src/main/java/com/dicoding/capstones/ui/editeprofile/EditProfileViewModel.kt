package com.dicoding.capstones.ui.editeprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.ApiConfig
import com.dicoding.capstones.network.EditProfileResponse
import com.dicoding.capstones.network.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel : ViewModel() {
    private val _editProfile = MutableLiveData<EditProfileResponse>()
    val editProfile: LiveData<EditProfileResponse> = _editProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun editProfileWithPass(
        userId: String,
        photo: String,
        userPassword: String,
        userName: String,
        userPhone: String,
        userDob: String,
        userGender: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().editProfileWithPass(
            userId,
            photo,
            userPassword,
            userName,
            userPhone,
            userDob,
            userGender
        )
        client.enqueue(object : Callback<EditProfileResponse> {

            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _editProfile.value = response.body()
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun editProfile(
        userId: String,
        photo: String,
        userName: String,
        userPhone: String,
        userDob: String,
        userGender: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().editProfile(
            userId,
            photo,
            userName,
            userPhone,
            userDob,
            userGender
        )
        client.enqueue(object : Callback<EditProfileResponse> {

            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _editProfile.value = response.body()
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "EditProfileViewModel"
    }
}
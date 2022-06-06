package com.dicoding.capstones.ui.upgrade

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.ApiConfig
import com.dicoding.capstones.network.EditProfileResponse
import com.dicoding.capstones.network.UpgradeUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpgradeUserModel: ViewModel() {
    private val _upgradeUser = MutableLiveData<UpgradeUserResponse>()
    val upgrade: LiveData<UpgradeUserResponse> = _upgradeUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun upgradeUser(
        userId: String,
        idCard: String,
        teachExp: Int,
        subject: String,
        score: Int
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().upgradeUser(
            userId,
            idCard,
            teachExp,
            subject,
            score
        )
        client.enqueue(object : Callback<UpgradeUserResponse> {

            override fun onResponse(
                call: Call<UpgradeUserResponse>,
                response: Response<UpgradeUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upgradeUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<UpgradeUserResponse>, t: Throwable) {
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
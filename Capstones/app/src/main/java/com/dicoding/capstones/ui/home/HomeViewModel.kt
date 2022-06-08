package com.dicoding.capstones.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _home = MutableLiveData<List<HomeDataItem>>()
    val home: LiveData<List<HomeDataItem>> = _home

    private val _rating = MutableLiveData<List<RatingDataItem>>()
    val rating: LiveData<List<RatingDataItem>> = _rating

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getHomeData(userEmail: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().homeUser(userEmail)
        client.enqueue(object : Callback<HomeResponse> {

            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _home.value = response.body()?.data
                    Log.e(TAG, _home.value.toString())
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getRatingData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRating()
        client.enqueue(object : Callback<RatingResponse> {

            override fun onResponse(call: Call<RatingResponse>, response: Response<RatingResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _rating.value = response.body()?.data
                    Log.e(TAG, _rating.value.toString())
                }
            }

            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
package com.dicoding.capstones.ui.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingRoomViewModel: ViewModel() {

    private val _orderComplete = MutableLiveData<OrderCompleteResponse>()
    val orderComplete: LiveData<OrderCompleteResponse> = _orderComplete

    private val _orderRating = MutableLiveData<OrderRatingResponse>()
    val orderRating: LiveData<OrderRatingResponse> = _orderRating

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun completeOrder(orderId: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().completeOrder(orderId)
        client.enqueue(object : Callback<OrderCompleteResponse> {

            override fun onResponse(call: Call<OrderCompleteResponse>, response: Response<OrderCompleteResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _orderComplete.value = response.body()
                }
            }

            override fun onFailure(call: Call<OrderCompleteResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun orderRating(orderId: String?, orderRating: Float?, orderDesc: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().orderRating(orderId, orderRating, orderDesc)
        client.enqueue(object : Callback<OrderRatingResponse> {

            override fun onResponse(call: Call<OrderRatingResponse>, response: Response<OrderRatingResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _orderRating.value = response.body()
                }
            }

            override fun onFailure(call: Call<OrderRatingResponse>, t: Throwable) {
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
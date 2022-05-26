package com.dicoding.capstones.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.ApiConfig
import com.dicoding.capstones.network.ProfileResponse
import com.dicoding.capstones.network.SearchDataItem
import com.dicoding.capstones.network.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchViewModel : ViewModel() {

    private val _ListUser = MutableLiveData<List<SearchDataItem>>()
    val listUser: LiveData<List<SearchDataItem>> = _ListUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun searchDataUser(query: String?){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList(query)
        client.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _ListUser.value = response.body()?.data
                    Log.e(TAG, _ListUser.value.toString())
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "profileViewModel"
    }


}
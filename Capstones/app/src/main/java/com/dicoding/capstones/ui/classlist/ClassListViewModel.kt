package com.dicoding.capstones.ui.classlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassListViewModel : ViewModel() {
    private val _classlist = MutableLiveData<List<ClassList>>()
    val classlist: LiveData<List<ClassList>> = _classlist

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getClassList(subjectName: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getClass(subjectName)
        client.enqueue(object : Callback<ClassResponse> {

            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _classlist.value = response.body()?.data
                    Log.e(TAG, _classlist.value.toString())
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
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
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

    private val _order = MutableLiveData<CreateOrderResponse>()
    val order: LiveData<CreateOrderResponse> = _order

    private val _orderlist = MutableLiveData<DataItem>()
    val orderlist: LiveData<DataItem> = _orderlist

    private val _dataStudent = MutableLiveData<List<ProfileDataItem>>()
    val dataStudent: LiveData<List<ProfileDataItem>> = _dataStudent

    private val _dataTeacher = MutableLiveData<List<ProfileDataItem>>()
    val dataTeacher: LiveData<List<ProfileDataItem>> = _dataTeacher

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

    fun createOrder(classId: String?, userId: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().createOrder(classId, userId)
        client.enqueue(object : Callback<CreateOrderResponse> {

            override fun onResponse(call: Call<CreateOrderResponse>, response: Response<CreateOrderResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _order.value = response.body()
                    getOrderlist("Student", userId)
                    Log.e(TAG, _classlist.value.toString())
                }
            }

            override fun onFailure(call: Call<CreateOrderResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getOrderlist(userRole: String?, userId: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getOrderOnGoing(userRole, userId)
        client.enqueue(object : Callback<OrderlistResponse> {

            override fun onResponse(call: Call<OrderlistResponse>, response: Response<OrderlistResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _orderlist.value = response.body()?.data?.last()
                    Log.e(TAG, _classlist.value.toString())
                }
            }

            override fun onFailure(call: Call<OrderlistResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDataStudent(userId: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().profile(userId)
        client.enqueue(object : Callback<ProfileResponse> {

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataStudent.value = response.body()?.data
                    Log.e(TAG, _classlist.value.toString())
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDataTeacher(userId: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().profile(userId)
        client.enqueue(object : Callback<ProfileResponse> {

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataTeacher.value = response.body()?.data
                    Log.e(TAG, _classlist.value.toString())
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "ClassListViewModel"
    }
}
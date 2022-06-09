package com.dicoding.capstones.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstones.network.*
import com.dicoding.capstones.ui.classlist.ClassListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<SearchDataItem>>()
    val listUser: LiveData<List<SearchDataItem>> = _listUser

    private val _predictions = MutableLiveData<List<PredictionsItem>>()
    val predictions: LiveData<List<PredictionsItem>> = _predictions

    private val _suggestions = MutableLiveData<List<SearchDataItem>>()
    val suggestions: LiveData<List<SearchDataItem>> = _suggestions

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

    fun searchDataUser(query: String?){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList(query)
        client.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getPredictions(userId: String?){
        _isLoading.value = true
        val client = ApiConfig.getApiService2().getPredictions(userId)
        client.enqueue(object : Callback<PredictionsResponse> {

            override fun onResponse(call: Call<PredictionsResponse>, response: Response<PredictionsResponse>) {
                if (response.isSuccessful) {
                    _predictions.value = response.body()?.predictions
                    val suggest1 = _predictions.value?.first()?.output2?.get(0)
                    val suggest2 = _predictions.value?.first()?.output2?.get(1)
                    val suggest3 = _predictions.value?.first()?.output2?.get(2)
                    updateSuggestions(userId, suggest1, suggest2, suggest3)
                } else {
                    _errorMessage.value = "Gagal Mendapatkan Rekomendasi Pengajar"
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<PredictionsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateSuggestions(userId: String?, suggest1: String?, suggest2: String?, suggest3: String?){
        val client = ApiConfig.getApiService().updateSuggest(userId, suggest1, suggest2, suggest3)
        client.enqueue(object : Callback<PredictionsResponse> {

            override fun onResponse(call: Call<PredictionsResponse>, response: Response<PredictionsResponse>) {
                if (response.isSuccessful) {
                    Log.e(TAG,"Success")
                    getSuggestions(userId)
                }
            }

            override fun onFailure(call: Call<PredictionsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSuggestions(userId: String?){
        val client = ApiConfig.getApiService().getSuggest(userId)
        client.enqueue(object : Callback<SuggestionsResponse> {

            override fun onResponse(call: Call<SuggestionsResponse>, response: Response<SuggestionsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _suggestions.value = response.body()?.data
                    Log.e(TAG, _suggestions.value.toString())
                }
            }

            override fun onFailure(call: Call<SuggestionsResponse>, t: Throwable) {
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

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
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
        private const val TAG = "profileViewModel"
    }


}
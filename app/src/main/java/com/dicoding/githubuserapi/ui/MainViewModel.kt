package com.dicoding.githubuserapi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapi.data.response.GithubResponse
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {
    private val _listProfile = MutableLiveData<List<ItemsItem>>()
    val listProfile :LiveData<List<ItemsItem>> = _listProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        showUser(username = "a")
    }

    fun searchUsername(username: String) {
        showUser(username)
    }

    private fun showUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse (
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
               _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _listProfile.value = response.body()?.items
                    } else {
                        Log.e(TAG, "onFailure:${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
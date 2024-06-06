package com.dicoding.githubuserapi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers : LiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing : LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        showFollow(username = "", follow = "followers")
    }

    fun showFollow(username : String, follow : String) {
        _isLoading.value = true
        val client = when (follow) {
            "following" -> ApiConfig.getApiService().getFollowing(username)
            "followers" -> ApiConfig.getApiService().getFollowers(username)
            else -> throw IllegalArgumentException("Unsupported type: $follow")
        }
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse (
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        when (follow) {
                            "followers" -> _listFollowers.value = response.body()
                            "following" -> _listFollowing.value = response.body()
                            else -> throw IllegalArgumentException("Unsupported type: $follow")
                        }
                    } else {
                        Log.e(TAG, "onFailure:${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}
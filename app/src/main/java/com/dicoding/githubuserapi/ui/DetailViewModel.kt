package com.dicoding.githubuserapi.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapi.data.response.DetailUserResponse
import com.dicoding.githubuserapi.data.retrofit.ApiConfig
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.database.UserDao
import com.dicoding.githubuserapi.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val mUserRepository : UserRepository = UserRepository(application)

    private val _detailProfile = MutableLiveData<DetailUserResponse>()
    val detailProfile : LiveData<DetailUserResponse> = _detailProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    init {
        showDetail("")
    }

    fun showDetail (username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse (
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _detailProfile.value = response.body()
                    } else {
                        Log.e(DetailViewModel.TAG, "onFailure:${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun checkUser(username: String): LiveData<Boolean> {
        return mUserRepository.checkUser(username)
    }

    fun insertUser (user: User) {
        mUserRepository.insert(user)
    }

    fun deleteUser(username: String) {
        mUserRepository.delete(username)
    }


}
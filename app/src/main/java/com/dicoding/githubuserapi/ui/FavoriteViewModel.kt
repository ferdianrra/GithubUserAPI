package com.dicoding.githubuserapi.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.repository.UserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val mUserRepository: UserRepository = UserRepository(application)

    fun  getAllFav(): LiveData<List<User>> {
        _isLoading.value = true
        val liveDataFav = mUserRepository.getAllUser()
        liveDataFav.observeForever {
            _isLoading.value = false
        }
        return liveDataFav
    }

}
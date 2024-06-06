package com.dicoding.githubuserapi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.database.UserDao
import com.dicoding.githubuserapi.database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//  Kelas ini berfungsi sebagai penghubung antara ViewModel dengan database atau resource data.
class UserRepository(application: Application) {
    private val mUserDao : UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUser() : LiveData<List<User>> =  mUserDao.getUser()

    fun checkUser(user: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()
        executorService.execute {
            val isFavorite = mUserDao.isUserFavorite(user)
            resultLiveData.postValue(isFavorite)
        }
        return resultLiveData
    }

    fun insert(user: User) {
        executorService.execute { mUserDao.insertUser(user) }
    }

    fun delete(user: String) {
        executorService.execute { mUserDao.deleteAll(user) }
    }


}
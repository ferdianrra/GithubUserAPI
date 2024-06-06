package com.dicoding.githubuserapi.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserapi.ui.DetailViewModel
import com.dicoding.githubuserapi.ui.FavoriteViewModel
import com.dicoding.githubuserapi.ui.SettingPreferences
import com.dicoding.githubuserapi.ui.SettingViewModel

// Kelas ini berfungsi untuk menambahkan context ketika memanggil kelas ViewModel di dalam Activity
class ViewModelFactory internal constructor(
    private val mApplication: Application?,
    private val settingPreferences: SettingPreferences?
    ) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, settingPreferences: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(null, settingPreferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, null)
                }
            }
            return INSTANCE as ViewModelFactory
        }

        @JvmStatic
        fun getInstance(settingPreferences: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(null, settingPreferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return mApplication?.let { DetailViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return mApplication?.let { FavoriteViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return settingPreferences?.let { SettingViewModel(it) } as T
        }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

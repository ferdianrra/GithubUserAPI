package com.dicoding.githubuserapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuserapi.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private var _favoriteUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _favoriteUserBinding

    private lateinit var adapter: favoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val favViewModel = obtainViewModel(this@FavoriteUserActivity)
        favViewModel.getAllFav().observe(this) {favUser ->
            if (favUser != null) {
                adapter.setFavUser(favUser)
            }
        }

        favViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding?.topAppBar?.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        adapter = favoriteUserAdapter()
        binding?.apply {
            FavRvView.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            FavRvView.setHasFixedSize(true)
            FavRvView.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(binding?.FavRvView?.context, DividerItemDecoration.VERTICAL)
            FavRvView.addItemDecoration(dividerItemDecoration)
        }

        adapter.setOnItemClickCallback(object: favoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(this@FavoriteUserActivity, DetailProfileActivity::class.java)
                intentToDetail.putExtra("LOGIN", data.username) // Mengirimkan data login saja
                startActivity(intentToDetail)
            }
        })
    }

    private fun obtainViewModel(favoriteUserActivity: FavoriteUserActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(favoriteUserActivity.application)
        return ViewModelProvider(favoriteUserActivity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showLoading(isloading: Boolean) {
        binding?.progressBar?.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteUserBinding = null
    }
}
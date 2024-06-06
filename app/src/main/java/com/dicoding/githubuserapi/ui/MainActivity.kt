package com.dicoding.githubuserapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapi.R
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.databinding.ActivityMainBinding
import com.dicoding.githubuserapi.helper.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.userRvView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.userRvView.addItemDecoration(itemDecoration)

        mainViewModel.listProfile.observe(this) { Reviewprofiles ->
            setReviewData(Reviewprofiles)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) { // Melakukan pencarian pada search view
            searchView.setupWithSearchBar(searchUsername)
            searchView
                .editText
                .setOnEditorActionListener { _,actionId,_ ->
                    searchView.hide()
                    val username = searchView.editText.text.toString().trim()
                    mainViewModel.searchUsername(username)
                    false
                }
        }

        binding.topAppBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId) {
                R.id.favoriteApp -> {
                    val intent = Intent(this, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.changeTheme -> {
                    val intent = Intent(this, SettingDarkModeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBar.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    private fun setReviewData(reviewprofiles: List<ItemsItem>) {
        val  adapter =ReviewUserAdapter()
        adapter.submitList(reviewprofiles)
        binding.userRvView.adapter = adapter
        if (reviewprofiles.isEmpty()) {
            binding.notFound.visibility = View.VISIBLE
        }
        else {
            binding.notFound.visibility = View.INVISIBLE
        }
        adapter.setOnItemClickCallback(object : ReviewUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(this@MainActivity, DetailProfileActivity::class.java)
                intentToDetail.putExtra("LOGIN", data.login) // Mengirimkan data login saja
                startActivity(intentToDetail)
            }
        })
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}


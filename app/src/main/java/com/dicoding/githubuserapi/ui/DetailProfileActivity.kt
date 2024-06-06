package com.dicoding.githubuserapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserapi.R
import com.dicoding.githubuserapi.data.response.DetailUserResponse
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.databinding.ActivityDetailProfileBinding
import com.dicoding.githubuserapi.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var detailViewModel: DetailViewModel
    private var user: User? = null
    private var checkUser = false
    private var linkUrl = ""
    private var avatarImg = ""
    private var followers = 0
    private var following = 0
    private val follow1ViewModel by viewModels<FollowViewModel>()

    fun getFollowViewModel(): FollowViewModel {
        return follow1ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = obtainViewModel(this@DetailProfileActivity)
        supportActionBar?.hide()
        val login = intent.getStringExtra("LOGIN").toString()
        val sharedPref = getSharedPreferences(KEY_PREFERED, MODE_PRIVATE)

        login?.let {
            Log.d(TAG, "Login: $it")
            detailViewModel.showDetail(it)
        }

        val sectionsPagerAdapter = SectionFollsAdapter(this)
        val viewPager : ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        sectionsPagerAdapter.username = login
        detailViewModel.detailProfile.observe(this) {DetailProfiles ->
            setDetailProfile(DetailProfiles)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                if (position == 0) {
                    tab.text = String.format(TAB_TITLES[position], followers)
                }
                else {
                    tab.text = String.format(TAB_TITLES[position], following )
                }

            }.attach()
        }

        binding.favorite.setOnClickListener {
            detailViewModel.checkUser(login).observe(this) { isFav ->
                if (isFav) {
                    detailViewModel.deleteUser(login)
                    sharedPref.edit().putBoolean(login, false).apply()
                    binding.favorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    val user = User (
                        username = login,
                        avatar = avatarImg,
                        link = linkUrl,
                        true
                    )
                    detailViewModel.insertUser(user)
                    user?.let { it1 -> detailViewModel.insertUser(it1) }
                    sharedPref.edit().putBoolean(login, true).apply()
                    binding.favorite.setImageResource(R.drawable.ic_fullfavorite)
                }
            }

        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val isFavoriteFromSharedPref = sharedPref.getBoolean(login, false)
        binding.favorite.setImageResource(
            if (isFavoriteFromSharedPref) R.drawable.ic_fullfavorite else R.drawable.ic_favorite
        )

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(DetailViewModel::class.java)
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBars.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    private fun setDetailProfile(detailProfiles: DetailUserResponse) {
        Log.d(TAG, "Setting detail profile: $detailProfiles")
        binding.name.text = detailProfiles.name ?: "s"
        binding.username.text = detailProfiles.login
        followers = detailProfiles.followers ?: 0
        following = detailProfiles.following ?: 0
        linkUrl = detailProfiles.url
        avatarImg = detailProfiles.avatarUrl.toString()
        Glide.with(this)
            .load("${detailProfiles.avatarUrl}")
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.photoProfile)
    }

    companion object {
        val TAG = "DetailProfileActivity"
        private val TAB_TITLES =  arrayOf(
            "Followers \n%d",
            "Following \n%d",
        )
        private val KEY_PREFERED = "key_prefered"
    }

}
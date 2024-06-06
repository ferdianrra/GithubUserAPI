package com.dicoding.githubuserapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionFollsAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollsFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollsFragment.ARG_POSITION, position )
            putString(FollsFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}
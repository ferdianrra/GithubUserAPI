package com.dicoding.githubuserapi.ui

import android.content.Intent
import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.databinding.FragmentFollsBinding

class FollsFragment : Fragment() {

    var usernames = ""
    private var position: Int = 0
    private var _binding: FragmentFollsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = getActivity() as DetailProfileActivity
        val followViewModel = activity.getFollowViewModel()

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        followViewModel.listFollowers.observe(viewLifecycleOwner) {Reviewprofiles ->
            if (position == 0) {
                setReviewData(Reviewprofiles)
            }
        }
        followViewModel.listFollowing.observe(viewLifecycleOwner) {Following ->
            if (position != 0) {
                setReviewData(Following)
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.FollowRvView?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.FollowRvView?.addItemDecoration(itemDecoration)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            usernames = it.getString(ARG_USERNAME).toString()
        }

        if (position == 0){
            followViewModel.showFollow(usernames, "followers")
        } else {
            followViewModel.showFollow(usernames, "following")
        }
    }

    companion object {
        val ARG_POSITION = "position"
        val ARG_USERNAME = "usernamess"
    }

    private fun setReviewData(reviewprofiles: List<ItemsItem>) {
        val  adapter =ReviewUserAdapter()
        adapter.submitList(reviewprofiles)
        binding?.FollowRvView?.adapter = adapter
        adapter.setOnItemClickCallback(object : ReviewUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(requireContext(), DetailProfileActivity::class.java)
                intentToDetail.putExtra("LOGIN", data.login)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isloading: Boolean) {
        binding?.progressBar?.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
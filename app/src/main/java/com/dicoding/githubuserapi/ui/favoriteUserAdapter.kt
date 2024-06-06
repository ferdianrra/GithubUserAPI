package com.dicoding.githubuserapi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserapi.data.response.ItemsItem
import com.dicoding.githubuserapi.database.User
import com.dicoding.githubuserapi.databinding.AccountReviewBinding
import com.dicoding.githubuserapi.helper.UserDiffCallback
import com.dicoding.githubuserapi.ui.ReviewUserAdapter.Companion.DIFF_CALLBACK
import kotlinx.coroutines.NonDisposableHandle.parent

class favoriteUserAdapter: RecyclerView.Adapter<favoriteUserAdapter.FavViewHolder>() {

    private val favUser = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setFavUser(favUser: List<User>) {
        val diffCallback = UserDiffCallback(this.favUser, favUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.favUser.clear()
        this.favUser.addAll(favUser)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = AccountReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favUser.size
    }

    override fun onBindViewHolder(holder: favoriteUserAdapter.FavViewHolder, position: Int) {
        val review = favUser[position]
        holder.bind(review)
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(review)}
    }

    inner class FavViewHolder(private val binding: AccountReviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                binding.nameReview.text = user.username
                Glide.with(binding.root)
                    .load(user.avatar)
                    .apply(RequestOptions().transform(CircleCrop()))
                    .into(binding.person)

            }
        }
    }


}
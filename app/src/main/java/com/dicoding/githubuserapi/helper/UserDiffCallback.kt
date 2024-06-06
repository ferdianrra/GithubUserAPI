package com.dicoding.githubuserapi.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuserapi.database.User

class UserDiffCallback(private val oldFavList: List<User>, private val newFavList: List<User>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavList.size

    override fun getNewListSize(): Int = newFavList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavList[oldItemPosition].username == newFavList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavList[oldItemPosition]
        val newNote = newFavList[newItemPosition]
        return oldNote.username == newNote.username && oldNote.avatar == newNote.avatar
    }

}
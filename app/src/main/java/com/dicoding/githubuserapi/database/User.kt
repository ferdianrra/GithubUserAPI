package com.dicoding.githubuserapi.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "avatar")
    var avatar: String? = " ",

    @ColumnInfo(name = "link")
    var link: String?,

    @ColumnInfo(name = "favorite")
    var isVaforite: Boolean = false
) : Parcelable
package com.dicoding.githubuserapi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getUser(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Query("DELETE FROM user WHERE username = :username")
    fun deleteAll(username: String?)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username AND favorite = 1)")
    fun isUserFavorite(username: String?): Boolean
}
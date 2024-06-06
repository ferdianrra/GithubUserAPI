package com.dicoding.githubuserapi.data.retrofit
import com.dicoding.githubuserapi.data.response.DetailUserResponse
import com.dicoding.githubuserapi.data.response.GithubResponse
import com.dicoding.githubuserapi.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    fun getListUser(
        @Query("q") q : String,
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}
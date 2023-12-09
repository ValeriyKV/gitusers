package com.example.gitApp.api.user

import com.example.gitApp.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import java.net.URI

interface UserAPI {

    @GET("/users")
    fun getUsers(): Call<List<User>>

    @GET
    fun getUserInfo(@Url url: String): Call<User>

}
package com.example.gitApp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {

    companion object{
        val url = "https://api.github.com/"
        fun getClient() : Retrofit{
            return APIClient().createClient()
        }
    }

    private fun createClient() : Retrofit{
        return Retrofit.Builder().client(OkHttpClient())
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
package com.example.wallpaperapp.apis

import com.example.wallpaperapp.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofitHelper = Retrofit
                                .Builder().baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create()).build()

    val apiInterface : ApiInterface = retrofitHelper.create(ApiInterface::class.java)
}
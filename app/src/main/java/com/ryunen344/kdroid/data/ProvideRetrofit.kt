package com.ryunen344.kdroid.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ProvideRetrofit(okHttpClient : OkHttpClient) : Retrofit{
    return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.github.com")
            // insert converter
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
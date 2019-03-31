package com.ryunen344.kdroid.di.provider

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.AsyncTwitter
import twitter4j.AsyncTwitterFactory




class AppProvider{

    fun provideOkhttpClient() : OkHttpClient {
        //var client : OkHttpClient.Builder = OkHttpClient.Builder()
        var client = OkHttpClient()
        return client
    }

    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
                .client(provideOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://api.fixer.io")
                .build()
    }

    fun provideTwitter() : Twitter {
        return TwitterFactory.getSingleton()
        //return AsyncTwitterFactory().instance
    }
}
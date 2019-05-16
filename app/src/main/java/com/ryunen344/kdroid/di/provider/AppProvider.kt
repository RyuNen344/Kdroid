package com.ryunen344.kdroid.di.provider

import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder

class AppProvider{

    lateinit var twitter : Twitter

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

    /**
     * this method has to call after config twitter instance
     */
    fun provideTwitter() : Twitter {
        return twitter
    }

    fun configureTwitter(builder : ConfigurationBuilder) {
        var configuration : Configuration = builder.build()
        var factory : TwitterFactory = TwitterFactory(configuration)
        twitter = factory.instance
    }

    fun providePiccaso(): Picasso {
        val picasso: Picasso = Picasso.get()
        //init picasso instance
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
        return picasso
    }
}
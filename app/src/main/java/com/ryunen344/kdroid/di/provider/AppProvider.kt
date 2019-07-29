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

    private lateinit var twitter : Twitter
    private var okHttpClient : OkHttpClient = OkHttpClient()
    private var picasso : Picasso = Picasso.get()
    private var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://google.co.jp")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    init {
        //init picasso instance
        picasso.setIndicatorsEnabled(false)
        picasso.isLoggingEnabled = false
    }

    fun provideRetrofit() : Retrofit {
        return retrofit
    }

    /**
     * this method has to call after config mTwitter instance
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
        return picasso
    }
}
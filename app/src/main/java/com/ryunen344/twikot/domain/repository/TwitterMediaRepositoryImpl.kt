package com.ryunen344.twikot.domain.repository

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TwitterMediaRepositoryImpl : TwitterMediaRepository {

    private var twitterMediaRepository : TwitterMediaRepository
    private var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://google.co.jp")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    init {
        twitterMediaRepository = retrofit.create(TwitterMediaRepository::class.java)
    }

    override fun getImageFromUrl(imageUrl : String) : Single<ResponseBody> {
        return twitterMediaRepository.getImageFromUrl(imageUrl)
    }

}
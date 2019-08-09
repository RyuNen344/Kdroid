package com.ryunen344.twikot.domain.repository

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface TwitterMediaRepository {

    @GET
    fun getImageFromUrl(@Url imageUrl : String) : Single<ResponseBody>

}
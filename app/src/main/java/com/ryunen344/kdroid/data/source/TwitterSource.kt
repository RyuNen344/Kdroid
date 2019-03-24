package com.ryunen344.kdroid.data.source

import com.ryunen344.kdroid.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TwitterSource{
    @Headers(
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit2-Test"
    )
    @GET("/users/{username}")
    fun getOAuthToken(@Path("username") username: String) : Call<User>

    fun getHomeTimeLine() : Call<User>


}
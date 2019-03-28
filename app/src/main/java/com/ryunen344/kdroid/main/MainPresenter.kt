package com.ryunen344.kdroid.main

import android.util.Log
import com.ryunen344.kdroid.data.User
import com.ryunen344.kdroid.data.api.DataSource
import com.ryunen344.kdroid.di.provider.AppProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import twitter4j.Twitter

class MainPresenter(val mainView : MainContract.View,val appProvider : AppProvider) : MainContract.Presenter{

    init {
        mainView.setPresenter(this)
    }

    override fun editTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {

        val twitter : Twitter = appProvider.provideTwitter()
        val retrofit : Retrofit = appProvider.provideRetrofit()
        var service : DataSource = retrofit.create(DataSource::class.java)

        var login : String
        var type : String
        var name : String
         service.getUser("RyuNen344")
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) { // ステータスコード200番以上300番未満のときtrue
                            //Success !!
                            val user: User? = response.body()
                            if (user != null) {
                                Log.d("MainPresenter : user", user.toString())
                                Log.d("MainPresenter : login  ", user.login)
                                Log.d("MainPresenter : type  ", user.type)
                                Log.d("MainPresenter : name  ", user.name)
                                login = user.login
                                type = user.type
                                name = user.name

                                mainView.showTitle(login + type)
                                mainView.showDescription(name)
                            }
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }
                })

        mainView.showTitle("no set")
        mainView.showDescription("no set")
    }

}


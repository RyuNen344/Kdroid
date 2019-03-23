package com.ryunen344.kdroid.main

import android.util.Log
import com.ryunen344.kdroid.data.ProvideRetrofit
import com.ryunen344.kdroid.data.User
import com.ryunen344.kdroid.data.source.DataSource
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainPresenter(mainView : MainContract.View) : MainContract.Presenter{

    var mMainView : MainContract.View = mainView
    init {
        mMainView.setPresenter(this)
    }

    override fun editTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {

        val httpClient : OkHttpClient = OkHttpClient()
        val retrofit : Retrofit = ProvideRetrofit(httpClient)

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

                                mMainView.showTitle(login + type)
                                mMainView.showDescription(name)
                            }
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }
                })

        mMainView.showTitle("no set")
        mMainView.showDescription("no set")
    }

}


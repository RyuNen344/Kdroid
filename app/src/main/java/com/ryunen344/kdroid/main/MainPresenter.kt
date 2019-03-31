package com.ryunen344.kdroid.main

import android.content.SharedPreferences
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.nullableString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class MainPresenter(val mainView : MainContract.View,val appProvider : AppProvider,val preferences : SharedPreferences) : MainContract.Presenter{
    var token : String? by preferences.nullableString("token")
    var tokenSecret : String? by preferences.nullableString("tokenSecret")

    init {
        mainView.setPresenter(this)
    }

    override fun start() {
        loadTweetList()
    }

    override fun loadTweetList() {
        val twitter : Twitter = appProvider.provideTwitter()
        //var user : User = twitter.verifyCredentials()
        twitter.setOAuthConsumer("FY914IvJrO3kxcXPpx7hCDDQq","vbyCmfDAQljPVuGBso66F4k0vXffxCCGTWzIVobMmNsKUywVg9")
        twitter.oAuthAccessToken = AccessToken(token,tokenSecret)
        lateinit var tweetLsit : List<Status>
        GlobalScope.launch(Dispatchers.Main){
            async(Dispatchers.Default){
                tweetLsit = twitter.homeTimeline
            }.await().let {
                mainView.showTweetList(tweetLsit)
            }
        }
    }

    override fun openTweetDetail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


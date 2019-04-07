package com.ryunen344.kdroid.main

import android.app.Activity
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.data.dao.AccountDao
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class MainPresenter(val mainView : MainContract.View, val appProvider : AppProvider, val userId : Long) : MainContract.Presenter {

    init {
        mainView.setPresenter(this)
    }

    override fun start() {
        loadTweetList()
    }

    override fun loadTweetList() {
        AccountDatabase.getInstance()?.let { accountDatabase ->
            val accountDao : AccountDao = accountDatabase.accountDao()

            accountDao.loadAccountById(userId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                val twitter : Twitter = appProvider.provideTwitter()
                                twitter.oAuthAccessToken = AccessToken(it.token, it.tokenSecret)
                                lateinit var tweetLsit : List<Status>
                                GlobalScope.launch(Dispatchers.Main) {
                                    async(Dispatchers.Default) {
                                        tweetLsit = twitter.homeTimeline
                                    }.await().let {
                                        mainView.showTweetList(tweetLsit)
                                    }
                                }
                            },
                            { e -> e.printStackTrace() })
        }

    }

    override fun openTweetDetail() {
        debugLog("start")
        debugLog("end")
    }

    override fun result(requestCode : Int, resultCode : Int) {
        debugLog("start")
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> mainView.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> mainView.showFailTweet()
                }
            }
        }
        debugLog("end")
    }

}


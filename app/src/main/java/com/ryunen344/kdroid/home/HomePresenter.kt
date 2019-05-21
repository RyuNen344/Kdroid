package com.ryunen344.kdroid.home

import android.app.Activity
import android.os.Bundle
import com.ryunen344.kdroid.addTweetReply.AddTweetReplyActivity
import com.ryunen344.kdroid.data.dao.AccountDao
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.mediaViewer.MediaViewerActivity
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class HomePresenter(val homeView : HomeContract.View, val appProvider : AppProvider, val apiProvider : ApiProvider, val bundle : Bundle?) : HomeContract.Presenter {

    var twitter : Twitter = appProvider.provideTwitter()
    private var mUserId : Long = 0L
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        bundle?.let {
            mUserId = it.getLong(HomeActivity.INTENT_KEY_USER_ID, 0)
        }
        homeView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        if (mUserId == 0L) {
            homeView.showError(Throwable("user id not found"))
        } else {
            initTwitter()
        }
        debugLog("end")
    }

    override fun initTwitter() {
        debugLog("start")
        AccountDatabase.getInstance()?.let { accountDatabase ->
            val accountDao : AccountDao = accountDatabase.accountDao()

            accountDao.loadAccountById(mUserId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                twitter.oAuthAccessToken = AccessToken(it.token, it.tokenSecret)

                            },
                            { e ->
                                homeView.showError(e)
                            })
        }
        debugLog("end")
    }

    override fun addNewTweet() {
        homeView.showAddNewTweet()
    }

    override fun result(requestCode : Int, resultCode : Int) {
        debugLog("start")
        when (requestCode) {
            AddTweetReplyActivity.REQUEST_ADD_TWEET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> homeView.showSuccessfullyTweet()
                    Activity.RESULT_CANCELED -> homeView.showFailTweet()
                }
            }
            MediaViewerActivity.REQUEST_SHOW_MEDIA -> {
                debugLog("media finish()")
            }
        }
        debugLog("end")
    }

    override fun clearDisposable() {
        mCompositeDisposable.clear()
    }

}


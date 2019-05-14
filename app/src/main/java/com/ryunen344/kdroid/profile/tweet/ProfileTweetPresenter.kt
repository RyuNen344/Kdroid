package com.ryunen344.kdroid.profile.tweet

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter

class ProfileTweetPresenter(val profileView: ProfileTweetContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider) : ProfileTweetContract.Presenter {

    lateinit var tweetList: List<Status>
    var twitter: Twitter = appProvider.provideTwitter()
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        loadTweetList()
        debugLog("end")
    }

    override fun loadTweetList() {
        debugLog("start")
        var paging: Paging = Paging(1, 50)
        val disposable: Disposable = apiProvider.getUserTimelineByUserId(twitter, paging, 1366386504).subscribe(
                { list: List<Status> ->
                    tweetList = list
                    profileView.showTweetList(tweetList)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun loadMoreTweetList(currentPage: Int) {
        debugLog("start")
        var paging: Paging = Paging(1, 50)
        val disposable: Disposable = apiProvider.getUserTimelineByUserId(twitter, paging, 1366386504).subscribe(
                { list: List<Status> ->
                    tweetList = tweetList + list
                    profileView.showTweetList(tweetList)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }

}
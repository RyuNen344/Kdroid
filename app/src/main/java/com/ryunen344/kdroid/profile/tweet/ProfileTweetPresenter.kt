package com.ryunen344.kdroid.profile.tweet

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter

class ProfileTweetPresenter(val profileView: ProfileTweetContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val pagerPosition: Int) : ProfileTweetContract.Presenter {

    lateinit var tweetList: List<Status>
    var twitter: Twitter = appProvider.provideTwitter()
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadTweetList()
            1 -> loadFavoriteList()
        }
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

    override fun loadFavoriteList() {
        debugLog("start")
        var paging: Paging = Paging(1, 50)
        val disposable: Disposable = apiProvider.getUserFavoriteByUserId(twitter, paging, 1366386504).subscribe(
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

    override fun loadMoreList(currentPage: Int) {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadMoreTweetList(currentPage)
            1 -> loadMoreFavoriteList(currentPage)
        }
        debugLog("end")
    }

    private fun loadMoreTweetList(currentPage: Int) {
        debugLog("start")
        var paging: Paging = Paging(currentPage + 1, 100)
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

    private fun loadMoreFavoriteList(currentPage: Int) {
        debugLog("start")
        var paging: Paging = Paging(currentPage + 1, 100)
        val disposable: Disposable = apiProvider.getUserFavoriteByUserId(twitter, paging, 1366386504).subscribe(
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
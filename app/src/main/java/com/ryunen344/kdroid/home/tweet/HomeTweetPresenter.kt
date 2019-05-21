package com.ryunen344.kdroid.profile.tweet

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class HomeTweetPresenter(private val homeTweetView : HomeTweetContract.View, val appProvider : AppProvider, val apiProvider : ApiProvider, private val pagerPosition : Int, val userId : Long) : HomeTweetContract.Presenter {


    lateinit var tweetList : List<Status>
    var twitter : Twitter = appProvider.provideTwitter()
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        homeTweetView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadTimelineList()
            1 -> loadMentionLsit()
        }
        debugLog("end")
    }

    override fun loadTimelineList() {
        debugLog("start")
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                { list : List<Status> ->
                    tweetList = list
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun loadMentionLsit() {
        debugLog("start")
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable = apiProvider.getMention(twitter, paging).subscribe(
                { list : List<Status> ->
                    tweetList = list
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun loadMoreList(currentPage : Int) {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadMoreTimelineList(currentPage)
            1 -> loadMoreMentionLsit(currentPage)
        }
        debugLog("end")
    }

    private fun loadMoreTimelineList(currentPage : Int) {
        debugLog("start")
        var paging : Paging = Paging(currentPage + 1, 50)
        val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                { list : List<Status> ->
                    tweetList = tweetList + list
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    private fun loadMoreMentionLsit(currentPage : Int) {
        debugLog("start")
        var paging : Paging = Paging(currentPage + 1, 50)
        val disposable : Disposable = apiProvider.getMention(twitter, paging).subscribe(
                { list : List<Status> ->
                    tweetList = tweetList + list
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun openMedia(mediaUrl : String) {
        debugLog("start")
        homeTweetView.showMediaViewer(mediaUrl)
        debugLog("end")
    }

    override fun openTweetDetail() {
        debugLog("start")
        homeTweetView.showTweetDetail()
        debugLog("end")
    }

    override fun openProfile(user : User) {
        debugLog("start")
        homeTweetView.showProfile(user)
        debugLog("end")
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
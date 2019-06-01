package com.ryunen344.kdroid.home.tweet

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

    lateinit var tweetList : MutableList<Status>
    var twitter : Twitter = appProvider.provideTwitter()
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        homeTweetView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadTimelineList()
            1 -> loadMentionList()
        }
        debugLog("end")
    }

    override fun loadLeastList() {
        debugLog("start")
        when (pagerPosition) {
            0 -> loadLeastTimeLineList()
            1 -> loadLeastMentionList()
        }
        debugLog("end")
    }

    override fun loadTimelineList() {
        debugLog("start")
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                { list : MutableList<Status> ->
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

    override fun loadMentionList() {
        debugLog("start")
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable = apiProvider.getMention(twitter, paging).subscribe(
                { list : MutableList<Status> ->
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
                { list : MutableList<Status> ->
                    tweetList.plusAssign(list)
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
                { list : MutableList<Status> ->
                    tweetList.plusAssign(list)
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    private fun loadLeastTimeLineList() {
        debugLog("start")
        var paging : Paging = Paging().sinceId(tweetList.first().id)
        val disposable : Disposable = apiProvider.getTimeLine(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = (list + tweetList).toMutableList()
                    homeTweetView.showTweetList(tweetList)
                }
                , { e ->
            homeTweetView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    private fun loadLeastMentionList() {
        debugLog("start")
        var paging : Paging = Paging().sinceId(tweetList.first().id)
        val disposable : Disposable = apiProvider.getMention(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = (list + tweetList).toMutableList()
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

    override fun changeFavorite(position : Int, tweet : Status) {
        debugLog("start")
        val disposable : Disposable =
                if (!tweet.isFavorited)
                    apiProvider.createFavorite(twitter, tweet.id).subscribe(
                            {
                                homeTweetView.notifyStatusChange(position, it)
                            },
                            {
                                homeTweetView.showError(it)
                            })
                else
                    apiProvider.destroyFavorite(twitter, tweet.id).subscribe(
                            {
                                homeTweetView.notifyStatusChange(position, it)
                            },
                            {
                                homeTweetView.showError(it)
                            })

        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
package com.ryunen344.kdroid.home.tweet

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
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
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadTimelineList()
            1 -> loadMentionList()
        }
    }

    override fun loadLeastList() {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadLeastTimeLineList()
            1 -> loadLeastMentionList()
        }
    }

    override fun loadTimelineList() {
        LogUtil.d()
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
    }

    override fun loadMentionList() {
        LogUtil.d()
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
    }

    override fun loadMoreList(currentPage : Int) {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadMoreTimelineList(currentPage)
            1 -> loadMoreMentionList(currentPage)
        }
    }

    private fun loadMoreTimelineList(currentPage : Int) {
        LogUtil.d()
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
    }

    private fun loadMoreMentionList(currentPage: Int) {
        LogUtil.d()
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
    }

    private fun loadLeastTimeLineList() {
        LogUtil.d()
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
    }

    private fun loadLeastMentionList() {
        LogUtil.d()
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
    }

    override fun openMedia(mediaUrl : String) {
        LogUtil.d()
        homeTweetView.showMediaViewer(mediaUrl)
    }

    override fun openTweetDetail(tweet : Status) {
        LogUtil.d()
        homeTweetView.showTweetDetail(tweet)
    }

    override fun openProfile(user : User) {
        LogUtil.d()
        homeTweetView.showProfile(user)
    }

    override fun openProfile(screenName: String) {
        LogUtil.d()
        homeTweetView.showProfile(screenName)
    }

    override fun changeFavorite(position : Int, tweet : Status) {
        LogUtil.d()
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
    }

    override fun changeRetweet(position : Int, tweet : Status) {
        LogUtil.d()

        val disposable : Disposable =
                if (!tweet.isRetweetedByMe) {
                    apiProvider.createRetweet(twitter, tweet.id).subscribe(
                            {
                                LogUtil.d("create RT")
                                homeTweetView.notifyStatusChange(position, it.retweetedStatus)
                            },
                            {
                                homeTweetView.showError(it)
                            })
                } else {
                    apiProvider.destroyRetweet(twitter, tweet.currentUserRetweetId).subscribe(
                            {
                                LogUtil.d("destroy RT")
                                homeTweetView.notifyStatusChange(position, it)
                            },
                            {
                                homeTweetView.showError(it)
                            })
                }


        mCompositeDisposable.add(disposable)
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}
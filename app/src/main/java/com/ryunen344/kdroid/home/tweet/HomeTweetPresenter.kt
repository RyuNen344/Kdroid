package com.ryunen344.kdroid.home.tweet

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.repository.TwitterRepositoryImpl
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class HomeTweetPresenter(private val pagerPosition : Int, val userId : Long) : HomeTweetContract.Presenter, KoinComponent {

    lateinit var tweetList : MutableList<Status>
    private val appProvider : AppProvider by inject()
    private var twitter : Twitter = appProvider.provideTwitter()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()

    override lateinit var view : HomeTweetContract.View

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
        val disposable : Disposable = twitterRepositoryImpl.getTimeLine(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = list
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun loadMentionList() {
        LogUtil.d()
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable = twitterRepositoryImpl.getMention(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = list
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
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
        val disposable : Disposable = twitterRepositoryImpl.getTimeLine(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList.plusAssign(list)
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    private fun loadMoreMentionList(currentPage: Int) {
        LogUtil.d()
        var paging : Paging = Paging(currentPage + 1, 50)
        val disposable : Disposable = twitterRepositoryImpl.getMention(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList.plusAssign(list)
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    private fun loadLeastTimeLineList() {
        LogUtil.d()
        var paging : Paging = Paging().sinceId(tweetList.first().id)
        val disposable : Disposable = twitterRepositoryImpl.getTimeLine(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = (list + tweetList).toMutableList()
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    private fun loadLeastMentionList() {
        LogUtil.d()
        var paging : Paging = Paging().sinceId(tweetList.first().id)
        val disposable : Disposable = twitterRepositoryImpl.getMention(twitter, paging).subscribe(
                { list : MutableList<Status> ->
                    tweetList = (list + tweetList).toMutableList()
                    view.showTweetList(tweetList)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun openMedia(mediaUrl : String) {
        LogUtil.d()
        view.showMediaViewer(mediaUrl)
    }

    override fun openTweetDetail(tweet : Status) {
        LogUtil.d()
        view.showTweetDetail(tweet)
    }

    override fun openProfile(user : User) {
        LogUtil.d()
        view.showProfile(user)
    }

    override fun openProfile(screenName: String) {
        LogUtil.d()
        view.showProfile(screenName)
    }

    override fun changeFavorite(position : Int, tweet : Status) {
        LogUtil.d()
        val disposable : Disposable =
                if (!tweet.isFavorited)
                    twitterRepositoryImpl.createFavorite(twitter, tweet.id).subscribe(
                            {
                                view.notifyStatusChange(position, it)
                            },
                            {
                                view.showError(it)
                            })
                else
                    twitterRepositoryImpl.destroyFavorite(twitter, tweet.id).subscribe(
                            {
                                view.notifyStatusChange(position, it)
                            },
                            {
                                view.showError(it)
                            })

        mCompositeDisposable.add(disposable)
    }

    override fun changeRetweet(position : Int, tweet : Status) {
        LogUtil.d()

        val disposable : Disposable =
                if (!tweet.isRetweetedByMe) {
                    twitterRepositoryImpl.createRetweet(twitter, tweet.id).subscribe(
                            {
                                LogUtil.d("create RT")
                                view.notifyStatusChange(position, it.retweetedStatus)
                            },
                            {
                                view.showError(it)
                            })
                } else {
                    twitterRepositoryImpl.destroyRetweet(twitter, tweet.currentUserRetweetId).subscribe(
                            {
                                LogUtil.d("destroy RT")
                                view.notifyStatusChange(position, it)
                            },
                            {
                                view.showError(it)
                            })
                }


        mCompositeDisposable.add(disposable)
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}
package com.ryunen344.twikot.profile.tweet

import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.repository.TwitterRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class ProfileTweetPresenter(private val pagerPosition : Int, private val userId : Long, private val screenName : String) : ProfileTweetContract.Presenter, KoinComponent {

    lateinit var tweetList : MutableList<Status>
    private val appProvider : AppProvider by inject()
    private var twitter : Twitter = appProvider.provideTwitter()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()

    override lateinit var view : ProfileTweetContract.View

    override fun start() {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadTweetList()
            1 -> loadFavoriteList()
        }
    }

    override fun loadTweetList() {
        LogUtil.d()
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserTimelineByScreenName(twitter, paging, screenName).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList = list
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }

                    else -> {
                        twitterRepositoryImpl.getUserTimelineByUserId(twitter, paging, userId).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList = list
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadFavoriteList() {
        LogUtil.d()
        var paging : Paging = Paging(1, 50)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserFavoriteByScreenName(twitter, paging, screenName).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList = list
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }

                    else -> {
                        twitterRepositoryImpl.getUserFavoriteByUserId(twitter, paging, userId).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList = list
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadMoreList(currentPage : Int) {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadMoreTweetList(currentPage)
            1 -> loadMoreFavoriteList(currentPage)
        }
    }

    private fun loadMoreTweetList(currentPage : Int) {
        LogUtil.d()
        var paging : Paging = Paging(currentPage + 1, 100)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserTimelineByScreenName(twitter, paging, screenName).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }

                    else -> {
                        twitterRepositoryImpl.getUserTimelineByUserId(twitter, paging, userId).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    private fun loadMoreFavoriteList(currentPage : Int) {
        LogUtil.d()
        var paging : Paging = Paging(currentPage + 1, 100)
        val disposable : Disposable =
                when (userId) {
                    0L -> {
                        twitterRepositoryImpl.getUserFavoriteByScreenName(twitter, paging, screenName).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                    else -> {
                        twitterRepositoryImpl.getUserFavoriteByUserId(twitter, paging, userId).subscribe(
                                { list : MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    view.showTweetList(tweetList)
                                }
                                , { e ->
                            view.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun openMedia(mediaUrl : String) {
        LogUtil.d()
        view.showMediaViewer(mediaUrl)
    }

    override fun openTweetDetail() {
        LogUtil.d()
        view.showTweetDetail()
    }

    override fun openProfile(user : User) {
        LogUtil.d()
        view.showProfile(user)
    }

    override fun openProfile(screenName : String) {
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
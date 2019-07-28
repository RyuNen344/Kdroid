package com.ryunen344.kdroid.profile.tweet

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.User

class ProfileTweetPresenter(val profileView: ProfileTweetContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val pagerPosition: Int, val userId: Long, val screenName: String) : ProfileTweetContract.Presenter {

    lateinit var tweetList: MutableList<Status>
    var twitter: Twitter = appProvider.provideTwitter()
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadTweetList()
            1 -> loadFavoriteList()
        }
    }

    override fun loadTweetList() {
        LogUtil.d()
        var paging: Paging = Paging(1, 50)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserTimelineByScreenName(twitter, paging, screenName).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList = list
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }

                    else -> {
                        apiProvider.getUserTimelineByUserId(twitter, paging, userId).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList = list
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadFavoriteList() {
        LogUtil.d()
        var paging: Paging = Paging(1, 50)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserFavoriteByScreenName(twitter, paging, screenName).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList = list
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }

                    else -> {
                        apiProvider.getUserFavoriteByUserId(twitter, paging, userId).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList = list
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun loadMoreList(currentPage: Int) {
        LogUtil.d()
        when (pagerPosition) {
            0 -> loadMoreTweetList(currentPage)
            1 -> loadMoreFavoriteList(currentPage)
        }
    }

    private fun loadMoreTweetList(currentPage: Int) {
        LogUtil.d()
        var paging: Paging = Paging(currentPage + 1, 100)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserTimelineByScreenName(twitter, paging, screenName).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }

                    else -> {
                        apiProvider.getUserTimelineByUserId(twitter, paging, userId).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    private fun loadMoreFavoriteList(currentPage: Int) {
        LogUtil.d()
        var paging: Paging = Paging(currentPage + 1, 100)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserFavoriteByScreenName(twitter, paging, screenName).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                    else -> {
                        apiProvider.getUserFavoriteByUserId(twitter, paging, userId).subscribe(
                                { list: MutableList<Status> ->
                                    tweetList.plusAssign(list)
                                    profileView.showTweetList(tweetList)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                }
        mCompositeDisposable.add(disposable)
    }

    override fun openMedia(mediaUrl: String) {
        LogUtil.d()
        profileView.showMediaViewer(mediaUrl)
    }

    override fun openTweetDetail() {
        LogUtil.d()
        profileView.showTweetDetail()
    }

    override fun openProfile(user: User) {
        LogUtil.d()
        profileView.showProfile(user)
    }

    override fun openProfile(screenName: String) {
        LogUtil.d()
        profileView.showProfile(screenName)
    }

    override fun changeFavorite(position: Int, tweet: Status) {
        LogUtil.d()
        val disposable: Disposable =
                if (!tweet.isFavorited)
                    apiProvider.createFavorite(twitter, tweet.id).subscribe(
                            {
                                profileView.notifyStatusChange(position, it)
                            },
                            {
                                profileView.showError(it)
                            })
                else
                    apiProvider.destroyFavorite(twitter, tweet.id).subscribe(
                            {
                                profileView.notifyStatusChange(position, it)
                            },
                            {
                                profileView.showError(it)
                            })

        mCompositeDisposable.add(disposable)
    }

    override fun changeRetweet(position: Int, tweet: Status) {
        LogUtil.d()

        val disposable: Disposable =
                if (!tweet.isRetweetedByMe) {
                    apiProvider.createRetweet(twitter, tweet.id).subscribe(
                            {
                                LogUtil.d("create RT")
                                profileView.notifyStatusChange(position, it.retweetedStatus)
                            },
                            {
                                profileView.showError(it)
                            })
                } else {
                    apiProvider.destroyRetweet(twitter, tweet.currentUserRetweetId).subscribe(
                            {
                                LogUtil.d("destroy RT")
                                profileView.notifyStatusChange(position, it)
                            },
                            {
                                profileView.showError(it)
                            })
                }


        mCompositeDisposable.add(disposable)
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}
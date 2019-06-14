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

class ProfileTweetPresenter(val profileView: ProfileTweetContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val pagerPosition: Int, val userId: Long, val screenName: String) : ProfileTweetContract.Presenter {

    lateinit var tweetList: MutableList<Status>
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
        debugLog("end")
    }

    override fun loadFavoriteList() {
        debugLog("start")
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
        debugLog("end")
    }

    private fun loadMoreFavoriteList(currentPage: Int) {
        debugLog("start")
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
        debugLog("end")
    }

    override fun openMedia(mediaUrl: String) {
        debugLog("start")
        profileView.showMediaViewer(mediaUrl)
        debugLog("end")
    }

    override fun openTweetDetail() {
        debugLog("start")
        profileView.showTweetDetail()
        debugLog("end")
    }

    override fun openProfile(user: User) {
        debugLog("start")
        profileView.showProfile(user)
        debugLog("end")
    }

    override fun openProfile(screenName: String) {
        debugLog("start")
        profileView.showProfile(screenName)
        debugLog("end")
    }

    override fun changeFavorite(position: Int, tweet: Status) {
        debugLog("start")
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
        debugLog("end")
    }

    override fun changeRetweet(position: Int, tweet: Status) {
        debugLog("start")

        val disposable: Disposable =
                if (!tweet.isRetweetedByMe) {
                    apiProvider.createRetweet(twitter, tweet.id).subscribe(
                            {
                                debugLog("create RT")
                                profileView.notifyStatusChange(position, it.retweetedStatus)
                            },
                            {
                                profileView.showError(it)
                            })
                } else {
                    apiProvider.destroyRetweet(twitter, tweet.currentUserRetweetId).subscribe(
                            {
                                debugLog("destroy RT")
                                profileView.notifyStatusChange(position, it)
                            },
                            {
                                profileView.showError(it)
                            })
                }


        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
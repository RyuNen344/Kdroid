package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.User

class ProfileUserPresenter(val profileView: ProfileUserContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val pagerPosition: Int, val userId: Long, val screenName: String) : ProfileUserContract.Presenter {

    lateinit var userList: List<User>
    var mTwitter: Twitter = appProvider.provideTwitter()
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        when (pagerPosition) {
            2 -> loadFollowList()
            3 -> loadFollowerList()
        }
        debugLog("end")
    }

    override fun loadFollowList() {
        debugLog("start")
        var pageing: Paging = Paging(1, 50)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserFollowByScreenName(mTwitter, screenName, -1).subscribe(
                                { list: List<User> ->
                                    userList = list
                                    profileView.showUserList(list)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                    else -> {
                        apiProvider.getUserFollowByUserId(mTwitter, userId, -1).subscribe(
                                { list: List<User> ->
                                    userList = list
                                    profileView.showUserList(list)
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

    override fun loadFollowerList() {
        debugLog("start")
        var pageing: Paging = Paging(1, 50)
        val disposable: Disposable =
                when (userId) {
                    0L -> {
                        apiProvider.getUserFollowerByScreenName(mTwitter, screenName, -1).subscribe(
                                { list: List<User> ->
                                    userList = list
                                    profileView.showUserList(list)
                                }
                                , { e ->
                            profileView.showError(e)
                        }
                        )
                    }
                    else -> {
                        apiProvider.getUserFollowerByUserId(mTwitter, userId, -1).subscribe(
                                { list: List<User> ->
                                    userList = list
                                    profileView.showUserList(list)
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
            2 -> loadMoreFollowList(currentPage)
            3 -> loadMoreFollowerList(currentPage)
        }
        debugLog("end")
    }

    private fun loadMoreFollowList(currentPage: Int) {
        debugLog("start")
        debugLog("end")
    }

    private fun loadMoreFollowerList(currentPage: Int) {
        debugLog("start")
        debugLog("end")
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
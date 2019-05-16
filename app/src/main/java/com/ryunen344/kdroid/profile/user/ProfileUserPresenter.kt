package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.User

class ProfileUserPresenter(val profileView: ProfileUserContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val pagerPosition: Int) : ProfileUserContract.Presenter {

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
        val disposable: Disposable = apiProvider.getUserFollowByUserId(mTwitter, 1366386504, -1).subscribe(
                { list: List<User> ->
                    userList = list
                    profileView.showUserList(list)
                }

        )

    }

    override fun loadFollowerList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMoreList(currentPage: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearDisposable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
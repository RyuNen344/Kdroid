package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog

class ProfileUserPresenter(val profileView: ProfileUserContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider) : ProfileUserContract.Presenter {

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        debugLog("end")
    }

    override fun loadTweetList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMoreTweetList(currentPage: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearDisposable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog

class ProfilePresenter(val profileView: ProfileContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val userId: Long) : ProfileContract.Presenter {

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
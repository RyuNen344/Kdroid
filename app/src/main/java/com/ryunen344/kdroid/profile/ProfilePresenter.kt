package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.main.MainContract
import com.ryunen344.kdroid.util.debugLog

class ProfilePresenter(val profileView: ProfileContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider) : ProfileContract.Presenter, MainContract.Presenter {

    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        debugLog("end")
    }

}
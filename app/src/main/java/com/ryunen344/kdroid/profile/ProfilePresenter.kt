package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Twitter

class ProfilePresenter(val profileView: ProfileContract.View, val appProvider: AppProvider, val apiProvider: ApiProvider, val userId: Long, val infoListener: ProfileContract.ProfileInfoListener) : ProfileContract.Presenter {

    var twitter: Twitter = appProvider.provideTwitter()
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    init {
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        loadProfile(userId)
        debugLog("end")
    }

    override fun loadProfile(userId: Long) {
        debugLog("start")
        val disposable: Disposable = apiProvider.getUserFromUserId(twitter, userId).subscribe(
                {
                    profileView.showUserInfo(it)
                    infoListener.showUserInfo(it)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")

    }


    override fun clearDisposable() {
        mCompositeDisposable.clear()
    }

}
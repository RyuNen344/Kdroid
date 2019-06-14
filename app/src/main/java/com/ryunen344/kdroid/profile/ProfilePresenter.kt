package com.ryunen344.kdroid.profile

import android.os.Bundle
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import twitter4j.Twitter

class ProfilePresenter(val profileView: ProfileContract.View, val appProvider: AppProvider, private val apiProvider: ApiProvider, val bundle: Bundle?, private val infoListener: ProfileContract.ProfileInfoListener) : ProfileContract.Presenter {

    private var mTwitter: Twitter = appProvider.provideTwitter()
    private var mUserId: Long = 0L
    private var mScreenName: String = ""
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        bundle?.let { it ->
            mUserId = it.getLong(ProfileActivity.INTENT_KEY_USER_ID, 0)
            it.getString(ProfileActivity.INTENT_KEY_SCREEN_NAME).let { screenName ->
                mScreenName = screenName!!
            }
        }
        profileView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        when {
            mUserId != 0L -> loadProfile(mUserId)
            mScreenName.isNotEmpty() -> loadProfile(mScreenName)
            else -> profileView.showError(Throwable("user id not found"))
        }
        debugLog("end")
    }

    override fun loadProfile(userId: Long) {
        debugLog("start")
        val disposable: Disposable = apiProvider.getUserByUserId(mTwitter, userId).subscribe(
                {
                    infoListener.showUserInfo(it)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
        debugLog("end")
    }

    override fun loadProfile(screenName: String) {
        debugLog("start")
        val disposable: Disposable = apiProvider.getUserByScreenName(mTwitter, screenName).subscribe(
                {
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
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
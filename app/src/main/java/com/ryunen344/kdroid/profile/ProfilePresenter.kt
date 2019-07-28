package com.ryunen344.kdroid.profile

import android.os.Bundle
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.ensureNotNull
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
            ensureNotNull(it.getString(ProfileActivity.INTENT_KEY_SCREEN_NAME)) {
                mScreenName = it
            }
        }
        profileView.setPresenter(this)
    }

    override fun start() {
        LogUtil.d()
        when {
            mUserId != 0L -> loadProfile(mUserId)
            mScreenName.isNotEmpty() -> loadProfile(mScreenName)
            else -> profileView.showError(Throwable("user id not found"))
        }
    }

    override fun loadProfile(userId: Long) {
        LogUtil.d()
        val disposable: Disposable = apiProvider.getUserByUserId(mTwitter, userId).subscribe(
                {
                    infoListener.showUserInfo(it)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun loadProfile(screenName: String) {
        LogUtil.d()
        val disposable: Disposable = apiProvider.getUserByScreenName(mTwitter, screenName).subscribe(
                {
                    infoListener.showUserInfo(it)
                }
                , { e ->
            profileView.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }


    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}
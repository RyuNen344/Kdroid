package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.repository.TwitterRepositoryImpl
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Twitter

class ProfilePresenter(private val userId : Long, private val screenName : String) : ProfileContract.Presenter, KoinComponent {

    private val appProvider : AppProvider by inject()
    private var mTwitter: Twitter = appProvider.provideTwitter()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    override lateinit var view : ProfileContract.View

    override fun start() {
        LogUtil.d()
        when {
            userId != 0L -> loadProfile(userId)
            screenName.isNotEmpty() -> loadProfile(screenName)
            else -> view.showError(Throwable("user id not found"))
        }
    }

    override fun loadProfile(userId: Long) {
        LogUtil.d()
        val disposable : Disposable = twitterRepositoryImpl.getUserByUserId(mTwitter, userId).subscribe(
                {
                    view.showUserInfo(it)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun loadProfile(screenName: String) {
        LogUtil.d()
        val disposable : Disposable = twitterRepositoryImpl.getUserByScreenName(mTwitter, screenName).subscribe(
                {
                    view.showUserInfo(it)
                }
                , { e ->
            view.showError(e)
        }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }
}
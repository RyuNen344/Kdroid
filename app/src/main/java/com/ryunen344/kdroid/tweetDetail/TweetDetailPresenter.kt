package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Twitter

class TweetDetailPresenter(private val tweetId : Long) : TweetDetailContract.Presenter, KoinComponent {

    private val apiProvider : ApiProvider by inject()
    private val appProvider : AppProvider by inject()
    private var mTwitter : Twitter = appProvider.provideTwitter()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    override lateinit var view : TweetDetailContract.View

    override fun start() {
        LogUtil.d()
        LogUtil.d(tweetId)
        loadTweetDetail()
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }

    override fun loadTweetDetail() {
        LogUtil.d()
        val disposable : Disposable =
                apiProvider.getTweetByTweetId(mTwitter, tweetId).subscribe(
                        { status ->
                            LogUtil.d(status)
                            view.showTweetDetail(status)

                        }
                        , { e ->
                    view.showError(e)
                }
                )
        mCompositeDisposable.add(disposable)
    }

}
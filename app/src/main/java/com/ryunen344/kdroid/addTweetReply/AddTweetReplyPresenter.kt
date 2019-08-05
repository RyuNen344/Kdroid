package com.ryunen344.kdroid.addTweetReply

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.domain.repository.TwitterRepositoryImpl
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Twitter


class AddTweetReplyPresenter() : AddTweetReplyContract.Presenter, KoinComponent {

    private val appProvider : AppProvider by inject()
    private var twitter : Twitter = appProvider.provideTwitter()
    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val twitterRepositoryImpl : TwitterRepositoryImpl by inject()

    override lateinit var view : AddTweetReplyContract.View

    override fun start() {
        //nop
    }

    override fun sendTweet(tweetDescription : String) {
        LogUtil.d(tweetDescription)
        val disposable : Disposable = twitterRepositoryImpl.updateStatus(twitter, tweetDescription).subscribe(
                {
                    LogUtil.d()
                    view.showTimeline()
                },
                {
                    LogUtil.e(it)
                    view.showError(it)
                })

        mCompositeDisposable.add(disposable)
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }


}
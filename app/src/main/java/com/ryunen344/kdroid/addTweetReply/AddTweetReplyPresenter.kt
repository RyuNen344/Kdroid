package com.ryunen344.kdroid.addTweetReply

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Twitter


class AddTweetReplyPresenter() : AddTweetReplyContract.Presenter, KoinComponent {

    private val appProvider : AppProvider by inject()
    private var mTwitter : Twitter = appProvider.provideTwitter()

    override lateinit var view : AddTweetReplyContract.View

    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        //nop
    }

    override fun sendTweet(tweetDescription : String) {
        LogUtil.d()
        LogUtil.d(tweetDescription)

        tweetDescription.let {
            Completable.create {
                mTwitter.updateStatus(tweetDescription)
                it.onComplete()
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                view.showTimeline()
                            },
                            {
                                view.showError(it)
                                LogUtil.e(it)
                            })
        }.run {
            LogUtil.d(tweetDescription)
        }

    }

}
package com.ryunen344.kdroid.addTweetReply

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.errorLog
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AddTweetReplyPresenter(var addTweetReplyView : AddTweetReplyContract.View, var appProvider : AppProvider) : AddTweetReplyContract.Presenter {

    init {
        addTweetReplyView.setPresenter(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        //nop
    }

    override fun sendTweet(tweetDescription : String) {
        debugLog("start")
        debugLog(tweetDescription)

        tweetDescription.let {
            Completable.create {
                appProvider.provideTwitter().updateStatus(tweetDescription)
                it.onComplete()
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                addTweetReplyView.showTimeline()
                            },
                            {
                                addTweetReplyView.showError(it)
                                errorLog(it.printStackTrace().toString())
                            })
        }.run {
            errorLog(tweetDescription)
        }

        debugLog("end")

    }

}
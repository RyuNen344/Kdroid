package com.ryunen344.kdroid.tweetDetail

import android.os.Bundle
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import twitter4j.Twitter

class TweetDetailPresenter(val tweetDetailView : TweetDetailContract.View, val appProvider : AppProvider, private val apiProvider : ApiProvider, val bundle : Bundle?) : TweetDetailContract.Presenter {

    private var mTwitter : Twitter = appProvider.provideTwitter()
    private var mTweetId : Long = 0L
    var mCompositeDisposable : CompositeDisposable = CompositeDisposable()

    init {
        bundle?.let { it ->
            mTweetId = it.getLong(TweetDetailActivity.INTENT_KEY_TWEET_ID, 0)
        }
        tweetDetailView.setPresenter(this)
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearDisposable() {
        debugLog("start")
        mCompositeDisposable.clear()
        debugLog("end")
    }
}
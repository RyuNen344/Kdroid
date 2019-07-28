package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.tweetDetail.TweetDetailActivity
import com.ryunen344.kdroid.tweetDetail.TweetDetailContract
import com.ryunen344.kdroid.tweetDetail.TweetDetailFragment
import com.ryunen344.kdroid.tweetDetail.TweetDetailPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppModule = module {
    single {AppProvider()}

    factory { TweetDetailFragment() }
    scope(named<TweetDetailFragment>()) {
        scoped { TweetDetailPresenter(getProperty(TweetDetailActivity.INTENT_KEY_TWEET_ID)) as TweetDetailContract.Presenter }
    }
}
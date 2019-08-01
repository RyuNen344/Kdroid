package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.home.HomeActivity
import com.ryunen344.kdroid.home.HomeContract
import com.ryunen344.kdroid.home.HomeFragment
import com.ryunen344.kdroid.home.HomePresenter
import com.ryunen344.kdroid.home.tweet.HomeTweetContract
import com.ryunen344.kdroid.home.tweet.HomeTweetFragment
import com.ryunen344.kdroid.home.tweet.HomeTweetPresenter
import com.ryunen344.kdroid.tweetDetail.TweetDetailActivity
import com.ryunen344.kdroid.tweetDetail.TweetDetailContract
import com.ryunen344.kdroid.tweetDetail.TweetDetailFragment
import com.ryunen344.kdroid.tweetDetail.TweetDetailPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppModule = module {
    single {AppProvider()}

    factory { HomeFragment() }
    scope(named<HomeFragment>()) {
        scoped { HomePresenter(getProperty(HomeActivity.INTENT_KEY_USER_ID)) as HomeContract.Presenter }
    }

    factory { HomeTweetFragment() }
    scope(named<HomeTweetFragment>()){
        scoped { HomeTweetPresenter(getProperty(HomeTweetFragment.INTENT_KEY_PAGER_POSITION), getProperty(HomeActivity.INTENT_KEY_USER_ID)) as HomeTweetContract.Presenter}
    }

    factory { TweetDetailFragment() }
    scope(named<TweetDetailFragment>()) {
        scoped { TweetDetailPresenter(getProperty(TweetDetailActivity.INTENT_KEY_TWEET_ID)) as TweetDetailContract.Presenter }
    }
}
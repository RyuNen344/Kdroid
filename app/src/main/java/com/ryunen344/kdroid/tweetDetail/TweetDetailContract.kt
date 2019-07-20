package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status

interface TweetDetailContract {

    interface View : BaseView<Presenter> {
        fun showTweetDetail(status : Status)
    }

    interface Presenter : BasePresenter {
        fun loadTweetDetail()
        fun clearDisposable()
    }

}
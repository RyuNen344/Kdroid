package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface TweetDetailContract {

    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter {
        fun clearDisposable()
    }

}
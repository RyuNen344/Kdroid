package com.ryunen344.kdroid.addTweetReply

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface AddTweetReplyContract {

    interface View : BaseView<Presenter> {

        fun showTimeline()
        fun showError(e : Throwable)

    }

    interface Presenter : BasePresenter {

        fun sendTweet(tweetDescription : String)

    }

}
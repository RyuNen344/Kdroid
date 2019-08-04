package com.ryunen344.kdroid.addTweetReply

import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView

interface AddTweetReplyContract {

    interface View : PreBaseView<Presenter> {
        fun showTimeline()
    }

    interface Presenter : PreBasePresenter<View> {
        fun sendTweet(tweetDescription : String)
    }

}
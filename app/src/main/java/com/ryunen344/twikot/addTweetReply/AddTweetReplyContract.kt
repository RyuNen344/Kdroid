package com.ryunen344.twikot.addTweetReply

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

interface AddTweetReplyContract {

    interface View : PreBaseView<Presenter> {
        fun showTimeline()
    }

    interface Presenter : PreBasePresenter<View> {
        fun sendTweet(tweetDescription : String)
        fun clearDisposable()
    }

}
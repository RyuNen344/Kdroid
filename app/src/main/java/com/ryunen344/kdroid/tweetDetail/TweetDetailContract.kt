package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView
import twitter4j.Status
import twitter4j.User

interface TweetDetailContract {

    interface View : PreBaseView<Presenter> {
        fun showTweetDetail(status : Status)
    }

    interface Presenter : PreBasePresenter<View> {
        fun loadTweetDetail()
        fun clearDisposable()
    }

    interface TweetItemListener {
        fun onAccountClick(user : User)
        fun onAccountClick(screenName : String)
        fun onImageClick(mediaUrl : String)
        fun onTweetClick(tweet : Status)
        fun onTweetLongClick(position : Int, tweet : Status)
        fun onContextMenuClick(position : Int, tweet : Status)
    }

}
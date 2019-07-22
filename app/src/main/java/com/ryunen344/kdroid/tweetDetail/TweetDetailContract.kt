package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status
import twitter4j.User

interface TweetDetailContract {

    interface View : BaseView<Presenter> {
        fun showTweetDetail(status : Status)
    }

    interface Presenter : BasePresenter {
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
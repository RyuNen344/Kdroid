package com.ryunen344.kdroid.home.tweet

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status
import twitter4j.User

interface HomeTweetContract {

    interface View : BaseView<Presenter> {
        fun showTweetList(tweetList : MutableList<Status>)
        fun showMediaViewer(mediaUrl : String)
        fun showTweetDetail()
        fun showProfile(user : User)
        fun notifyStatusChange(position : Int, tweet : Status)
    }

    interface Presenter : BasePresenter {
        fun loadLeastList()
        fun loadTimelineList()
        fun loadMentionList()
        fun loadMoreList(currentPage : Int)
        fun openMedia(mediaUrl : String)
        fun openTweetDetail()
        fun openProfile(user : User)
        fun changeFavorite(position : Int, tweet : Status)
        fun clearDisposable()
    }

    interface TweetItemListener {
        fun onAccountClick(user : User)
        fun onImageClick(mediaUrl : String)
        fun onTweetClick()
        fun onTweetLongClick(position : Int, tweet : Status)
    }
}
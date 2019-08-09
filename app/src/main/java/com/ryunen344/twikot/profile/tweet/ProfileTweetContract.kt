package com.ryunen344.twikot.profile.tweet

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView
import twitter4j.Status
import twitter4j.User

interface ProfileTweetContract {

    interface View : PreBaseView<Presenter> {
        fun showTweetList(tweetList: MutableList<Status>)
        fun showMediaViewer(mediaUrl: String)
        fun showTweetDetail()
        fun showProfile(user: User)
        fun showProfile(screenName: String)
        fun showContextMenu(position: Int, tweet: Status)
        fun notifyStatusChange(position: Int, tweet: Status)
    }

    interface Presenter : PreBasePresenter<View> {
        fun loadTweetList()
        fun loadFavoriteList()
        fun loadMoreList(currentPage: Int)
        fun openMedia(mediaUrl: String)
        fun openTweetDetail()
        fun openProfile(user: User)
        fun openProfile(screenName: String)
        fun changeFavorite(position: Int, tweet: Status)
        fun changeRetweet(position: Int, tweet: Status)
        fun clearDisposable()
    }

    interface ProfileItemListener {
        fun onAccountClick(user: User)
        fun onAccountClick(screenName: String)
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
        fun onTweetLongClick(position: Int, tweet: Status)
        fun onContextMenuClick(position: Int, tweet: Status)
    }
}
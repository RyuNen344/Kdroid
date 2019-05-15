package com.ryunen344.kdroid.profile.tweet

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status

interface ProfileTweetContract {

    interface View : BaseView<Presenter> {
        fun showTweetList(mainList: List<Status>)
        fun showMediaViewer(mediaUrl: String)
        fun showTweetDetail()
        fun showProfile()
    }

    interface Presenter : BasePresenter {
        fun loadTweetList()
        fun loadFavoriteList()
        fun loadMoreList(currentPage: Int)
        fun clearDisposable()
    }

    interface ProfileItemListner {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
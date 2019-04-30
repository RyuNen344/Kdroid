package com.ryunen344.kdroid.main

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status

interface MainContract {

    interface View : BaseView<Presenter> {
        fun showTweetList(mainList : List<Status>)
        fun showAddNewTweet()
        fun showMediaViewer(mediaUrl: String)
        fun showTweetDetail()
        fun showProfile()
        fun showSuccessfullyTweet()
        fun showFailTweet()
    }

    interface Presenter : BasePresenter {
        fun loadTweetList()
        fun loadMoreTweetList(currentPage: Int)
        fun addNewTweet()
        fun openMedia(mediaUrl: String)
        fun openTweetDetail()
        fun openProfile()
        fun result(requestCode : Int, resultCode : Int)
        fun clearDisposable()
    }

    interface MainItemListner {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
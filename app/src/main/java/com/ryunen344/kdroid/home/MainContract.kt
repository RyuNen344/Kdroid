package com.ryunen344.kdroid.home

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status
import twitter4j.User

interface MainContract {

    interface View : BaseView<Presenter> {
        fun showTweetList(mainList : List<Status>)
        fun showAddNewTweet()
        fun showMediaViewer(mediaUrl : String)
        fun showTweetDetail()
        fun showProfile(user : User)
        fun showSuccessfullyTweet()
        fun showFailTweet()
    }

    interface Presenter : BasePresenter {
        fun loadTweetList()
        fun loadMoreTweetList(currentPage : Int)
        fun addNewTweet()
        fun openMedia(mediaUrl : String)
        fun openTweetDetail()
        fun openProfile(user : User)
        fun result(requestCode : Int, resultCode : Int)
        fun clearDisposable()
    }

    interface MainItemListner {
        fun onAccountClick(user : User)
        fun onImageClick(mediaUrl : String)
        fun onTweetClick()
    }
}
package com.ryunen344.kdroid.home

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.User

interface HomeContract {

    interface View : BaseView<Presenter> {
        fun showAddNewTweet()
        fun showSuccessfullyTweet()
        fun showFailTweet()
    }

    interface Presenter : BasePresenter {
        fun initTwitter()
        fun addNewTweet()
        fun result(requestCode : Int, resultCode : Int)
        fun clearDisposable()
    }

    interface MainItemListner {
        fun onAccountClick(user : User)
        fun onImageClick(mediaUrl : String)
        fun onTweetClick()
    }
}
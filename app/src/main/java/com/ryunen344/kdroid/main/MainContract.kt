package com.ryunen344.kdroid.main

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.Status

interface MainContract {

    interface View : BaseView<Presenter> {
        fun showTweetList(mainList : List<Status>)
        fun addNewTweet()
        fun showSuccessfullyTweet()
        fun showFailTweet()
        fun showError(e: Throwable)
    }

    interface Presenter : BasePresenter {
        fun loadTweetList()
        fun loadMoreTweetList(currentPage: Int)
        fun openTweetDetail()
        fun result(requestCode : Int, resultCode : Int)
        fun clearDisposable()
    }

    interface MainItemListner {
        fun onAccountClick()
    }
}
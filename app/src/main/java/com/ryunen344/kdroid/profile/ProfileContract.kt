package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface ProfileContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
        fun loadTweetList()
        fun loadMoreTweetList(currentPage: Int)
        fun clearDisposable()
    }

    interface ProfileItemListner {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
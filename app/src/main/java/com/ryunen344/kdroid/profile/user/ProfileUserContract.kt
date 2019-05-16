package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.User

interface ProfileUserContract {

    interface View : BaseView<Presenter> {
        fun showUserList(userList: List<User>)
        fun showMediaViewer(mediaUrl: String)
        fun showTweetDetail()
        fun showProfile()
    }

    interface Presenter : BasePresenter {
        fun loadFollowList()
        fun loadFollowerList()
        fun loadMoreList(currentPage: Int)
        fun clearDisposable()
    }

    interface ProfileItemListner {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
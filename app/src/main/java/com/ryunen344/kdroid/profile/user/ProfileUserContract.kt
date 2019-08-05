package com.ryunen344.kdroid.profile.user

import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView
import twitter4j.User

interface ProfileUserContract {

    interface View : PreBaseView<Presenter> {
        fun showUserList(userList: List<User>)
        fun showMediaViewer(mediaUrl: String)
        fun showTweetDetail()
        fun showProfile()
    }

    interface Presenter : PreBasePresenter<View> {
        fun loadFollowList()
        fun loadFollowerList()
        fun loadMoreList(currentPage: Int)
        fun clearDisposable()
    }

    interface ProfileItemListener {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
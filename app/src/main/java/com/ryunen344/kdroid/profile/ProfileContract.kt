package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.User

interface ProfileContract {

    interface View : BaseView<Presenter> {
        fun showUserInfo(user: User)

    }

    interface Presenter : BasePresenter {
        fun loadProfile(userId: Long)
        fun clearDisposable()
    }

    interface ProfileItemListener {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }

    interface ProfileInfoListener {
        fun showUserInfo(user: User)
    }
}
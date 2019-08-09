package com.ryunen344.twikot.profile

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView
import twitter4j.User

interface ProfileContract {

    interface View : PreBaseView<Presenter> {
        fun showUserInfo(user: User)
    }

    interface Presenter : PreBasePresenter<View> {
        fun loadProfile(userId: Long)
        fun loadProfile(screenName: String)
        fun clearDisposable()
    }

    interface ProfileItemListener {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
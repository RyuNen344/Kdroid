package com.ryunen344.kdroid.profile

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface ProfileContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }

    interface ProfileItemListner {
        fun onAccountClick()
        fun onImageClick(mediaUrl: String)
        fun onTweetClick()
    }
}
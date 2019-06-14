package com.ryunen344.kdroid.tweetDetail

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import com.ryunen344.kdroid.profile.ProfileContract

interface TweetDetailContract {

    interface View : BaseView<ProfileContract.Presenter> {

    }

    interface Presenter : BasePresenter {

    }

}
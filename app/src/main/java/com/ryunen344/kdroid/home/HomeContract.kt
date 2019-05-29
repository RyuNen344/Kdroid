package com.ryunen344.kdroid.home

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import twitter4j.User
import java.io.File

interface HomeContract {

    interface View : BaseView<Presenter> {
        fun showAddNewTweet()
        fun showSuccessfullyTweet()
        fun showFailTweet()
        fun showSuccessfullyUpdateProfile()
        fun showDrawerProfile(userName : String?, screenName : String, profileImage : String?, profileBannerImage : String?)
    }

    interface Presenter : BasePresenter {
        fun initTwitter(absoluteDirPath : String?)
        fun initProfile(absoluteDirPath : String?)
        fun checkImageStatus(internalFileDir : File?)
        fun addNewTweet()
        fun result(requestCode : Int, resultCode : Int)
        fun clearDisposable()
    }

    interface MainItemListener {
        fun onAccountClick(user : User)
        fun onImageClick(mediaUrl : String)
        fun onTweetClick()
    }
}
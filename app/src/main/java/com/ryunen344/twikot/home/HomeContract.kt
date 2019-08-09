package com.ryunen344.twikot.home

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView
import twitter4j.User
import java.io.File

interface HomeContract {

    interface View : PreBaseView<Presenter> {
        fun showAddNewTweet()
        fun showSuccessfullyTweet()
        fun showFailTweet()
        fun showSuccessfullyUpdateProfile()
        fun showDrawerProfile(userName : String?, screenName : String, profileImage : String?, profileBannerImage : String?)
    }

    interface Presenter : PreBasePresenter<View> {
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
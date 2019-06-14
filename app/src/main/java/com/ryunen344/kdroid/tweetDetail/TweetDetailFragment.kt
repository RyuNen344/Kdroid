package com.ryunen344.kdroid.tweetDetail

import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.profile.ProfileContract


class TweetDetailFragment : Fragment(), TweetDetailContract.View {


    override fun setPresenter(presenter: ProfileContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
package com.ryunen344.kdroid.profile.user

import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import org.koin.android.ext.android.inject

class ProfileUserFragment : Fragment(), ProfileUserContract.View {

    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileUserContract.Presenter

    companion object {
        fun newInstance() = ProfileUserFragment()
    }

    private var itemListner: ProfileUserContract.ProfileItemListner = object : ProfileUserContract.ProfileItemListner {
        override fun onAccountClick() {
            debugLog("start")
            debugLog("end")
        }

        override fun onImageClick(mediaUrl: String) {
            debugLog("start")
            debugLog("end")
        }

        override fun onTweetClick() {
            debugLog("start")
            debugLog("end")
        }

    }

    override fun setPresenter(presenter: ProfileUserContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun showError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
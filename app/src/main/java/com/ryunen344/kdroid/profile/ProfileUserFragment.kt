package com.ryunen344.kdroid.profile

import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import org.koin.android.ext.android.inject

class ProfileUserFragment : Fragment(), ProfileContract.View {

    private val utilProvider: UtilProvider by inject()
    private lateinit var mPresenter: ProfileContract.Presenter

    companion object {
        fun newInstance() = ProfileUserFragment()
    }

    private var itemListner: ProfileContract.ProfileItemListner = object : ProfileContract.ProfileItemListner {
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

    override fun setPresenter(presenter: ProfileContract.Presenter) {
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
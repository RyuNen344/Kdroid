package com.ryunen344.kdroid.mediaViewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull

class MediaViewerFragment : Fragment(), MediaViewerContract.View {

    lateinit var mPresenter: MediaViewerContract.Presenter

    companion object {
        fun newInstance() = MediaViewerFragment()
    }

    override fun setPresenter(presenter: MediaViewerContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun showError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
package com.ryunen344.kdroid.tweetDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import org.koin.android.ext.android.inject


class TweetDetailFragment : Fragment(), TweetDetailContract.View {

    private val appProvider : AppProvider by inject()
    private val utilProvider : UtilProvider by inject()
    private lateinit var mPresenter : TweetDetailContract.Presenter

    companion object {
        fun newInstance() = TweetDetailFragment()
    }

    override fun setPresenter(presenter : TweetDetailContract.Presenter) {
        debugLog("start")
        presenter.let {
            mPresenter = it
        }
        debugLog("end")
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        debugLog("start")
        debugLog(container.toString())
        val root = inflater.inflate(R.layout.fragment_tweet_detail, container, false)
        debugLog("end")
        return root
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        debugLog("start")
        super.onActivityCreated(savedInstanceState)
        debugLog("end")
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        debugLog("start")
        mPresenter.start()
        debugLog("end")
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.clearDisposable()
    }

    override fun showError(e : Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
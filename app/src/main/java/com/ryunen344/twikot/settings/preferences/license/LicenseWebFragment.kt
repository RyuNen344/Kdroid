package com.ryunen344.twikot.settings.preferences.license

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.ryunen344.twikot.R.layout.fragment_license_web
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.fragment_license_web.view.*


class LicenseWebFragment : Fragment() {

    var mPagerPosition : Int = 1

    companion object {
        const val URL_PRIVACY = "https://twitter.com/privacy"
        const val URL_TOS = "https://twitter.com/tos"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d("mPagerPosition is $mPagerPosition")
        var root : View = inflater.inflate(fragment_license_web, container, false)

        root.license_web.apply {
            LogUtil.d("mPagerPosition is $mPagerPosition")
            this.loadUrl(
                    when (mPagerPosition) {
                        1 -> URL_PRIVACY
                        else -> URL_TOS
                    }
            )

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view : WebView?, url : String?) {
                    LogUtil.d()
                    super.onPageFinished(view, url)
                    view?.let {
                        it.settings.javaScriptEnabled = true
                    }
                }

                override fun shouldOverrideUrlLoading(view : WebView, url : String) : Boolean {
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    view.loadUrl(url)
                    return false // then it is not handled by default action
                }
            }
        }

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
    }
}
package com.ryunen344.kdroid.mediaViewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_media_viewer
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_media_viewer.*
import org.koin.android.ext.android.inject

class MediaViewerActivity : AppCompatActivity() {
    lateinit var mPresenter: MediaViewerContract.Presenter
    private val appProvider: AppProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        setContentView(activity_media_viewer)

        var mediaViewerFragment: MediaViewerFragment? = supportFragmentManager.findFragmentById(mediaViewerFrame.id) as MediaViewerFragment?
                ?: MediaViewerFragment.newInstance().also {
                    replaceFragmentInActivity(supportFragmentManager, it, mediaViewerFrame.id)
                }

        mPresenter = MediaViewerPresenter(mediaViewerFragment!!, appProvider)
        debugLog("end")
    }

}
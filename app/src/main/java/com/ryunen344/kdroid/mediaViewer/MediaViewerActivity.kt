package com.ryunen344.kdroid.mediaViewer

import android.Manifest
import android.content.pm.PackageManager
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

    companion object {
        val REQUEST_SHOW_MEDIA: Int = 20
        val INTENT_KEY_MEDIA_URL: String = "key_media_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        setContentView(activity_media_viewer)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        //requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

        var mediaViewerFragment: MediaViewerFragment? = supportFragmentManager.findFragmentById(mediaViewerFrame.id) as MediaViewerFragment?
                ?: MediaViewerFragment.newInstance().also {
                    replaceFragmentInActivity(supportFragmentManager, it, mediaViewerFrame.id)
                }

        mPresenter = MediaViewerPresenter(mediaViewerFragment!!, appProvider, intent.getStringExtra(INTENT_KEY_MEDIA_URL))
        debugLog("end")
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}
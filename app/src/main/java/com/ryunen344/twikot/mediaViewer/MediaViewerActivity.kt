package com.ryunen344.twikot.mediaViewer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R.layout.activity_media_viewer
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_media_viewer.*
import org.koin.android.ext.android.inject

class MediaViewerActivity : AppCompatActivity() {

    private val mediaViewerFragment : MediaViewerFragment by inject()

    companion object {
        val REQUEST_SHOW_MEDIA: Int = 20
        val INTENT_KEY_MEDIA_URL: String = "key_media_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_media_viewer)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        //requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

        var bundle : Bundle = Bundle()
        bundle.putString(INTENT_KEY_MEDIA_URL, intent.getStringExtra(INTENT_KEY_MEDIA_URL))
        mediaViewerFragment.arguments = bundle

        supportFragmentManager.findFragmentById(mediaViewerFrame.id) as MediaViewerFragment?
                ?: mediaViewerFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, mediaViewerFrame.id)
                }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}
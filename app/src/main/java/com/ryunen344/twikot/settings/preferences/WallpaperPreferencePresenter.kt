package com.ryunen344.twikot.settings.preferences

import android.app.Activity
import android.content.Intent
import com.ryunen344.twikot.settings.preferences.WallpaperPreferenceDialogFragmentCompat.Companion.REQUEST_IMAGE_GET
import com.ryunen344.twikot.util.LogUtil
import org.koin.core.KoinComponent

class WallpaperPreferencePresenter : WallpaperPreferenceContract.Presenter, KoinComponent {

    override lateinit var view : WallpaperPreferenceContract.View

    override fun doSomething() {
        LogUtil.d()
        view.doSomething()
    }

    override fun start() {
        LogUtil.d()
        view.doSomething()
    }

    override fun selectWallpaperImage() {
        LogUtil.d()
        view.openImagePicker()
    }

    override fun result(requestCode : Int, resultCode : Int, data : Intent?) {
        LogUtil.d()
        when (requestCode) {
            REQUEST_IMAGE_GET -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        LogUtil.d()
                        data?.let {
                            view.showWallpaperImage(it.data!!)
                        }
                    }

                    Activity.RESULT_CANCELED -> LogUtil.d()
                }
            }

        }

    }

}
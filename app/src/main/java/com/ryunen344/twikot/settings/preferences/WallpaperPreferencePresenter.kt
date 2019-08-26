package com.ryunen344.twikot.settings.preferences

import android.app.Activity
import android.content.Intent
import com.ryunen344.twikot.domain.repository.WallpaperRepositoryImpl
import com.ryunen344.twikot.settings.preferences.WallpaperPreferenceDialogFragmentCompat.Companion.REQUEST_IMAGE_GET
import com.ryunen344.twikot.util.LogUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class WallpaperPreferencePresenter : WallpaperPreferenceContract.Presenter, KoinComponent {

    override lateinit var view : WallpaperPreferenceContract.View

    private val wallpaperRepositoryImpl : WallpaperRepositoryImpl by inject()

    override fun start() {
        LogUtil.d()
        view.initView()
    }

    override fun selectWallpaperImage() {
        LogUtil.d()
        view.openImagePicker()
    }

    override fun loadSharedPreferences() {
        LogUtil.d()
        wallpaperRepositoryImpl.apply {
            view.setSharedPreferencesParam(this.getUri(), this.getSeekBarValue(), this.getCropState())
        }
    }

    override fun saveCurrentPreferences(uriString : String?, seekBarValue : Int, cropSwitchState : Boolean) {
        LogUtil.d()
        wallpaperRepositoryImpl.apply {
            this.setUri(uriString)
            this.setSeekBarValue(seekBarValue)
            this.setCropState(cropSwitchState)
        }
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
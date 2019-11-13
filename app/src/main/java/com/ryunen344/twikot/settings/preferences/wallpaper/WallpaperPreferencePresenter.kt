package com.ryunen344.twikot.settings.preferences.wallpaper

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.ryunen344.twikot.repository.WallpaperRepositoryImpl
import com.ryunen344.twikot.settings.preferences.wallpaper.WallpaperPreferenceDialogFragmentCompat.Companion.REQUEST_IMAGE_GET
import com.ryunen344.twikot.util.LogUtil
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.io.FileOutputStream

class WallpaperPreferencePresenter : WallpaperPreferenceContract.Presenter, KoinComponent {

    override lateinit var view : WallpaperPreferenceContract.View

    private val wallpaperRepositoryImpl : WallpaperRepositoryImpl by inject()

    lateinit var filePath : String
    var tempImage : Bitmap? = null

    override fun start() {
        LogUtil.d()
        view.initView()
    }

    override fun setAbsoluteFilePath(path : String) {
        LogUtil.d(path)
        filePath = path
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

        when (compareCurrentValueChange(uriString, seekBarValue, cropSwitchState)) {
            1 -> {
                LogUtil.d()
                saveWallpaperImage(uriString)
            }

            2 -> {
                LogUtil.d()
                wallpaperRepositoryImpl.apply {
                    this.setSeekBarValue(seekBarValue)
                }
            }

            3 -> {
                LogUtil.d()
                wallpaperRepositoryImpl.apply {
                    this.setCropState(cropSwitchState)
                }
            }

            else -> {
                LogUtil.d("nothing to save")
            }
        }
    }

    override fun saveWallpaperImage(uriString : String?) {
        LogUtil.d(filePath)
        wallpaperRepositoryImpl.setUri(filePath + File.separator + "wallpaper.png")
        val outStream = FileOutputStream(File(filePath + File.separator + "wallpaper.png"))
        tempImage?.let {
            it.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        }
        outStream.close()
    }

    override fun setTempBitMap(bitmap : Bitmap?) {
        LogUtil.d()
        tempImage = bitmap
    }


    private fun compareCurrentValueChange(uriString : String?, seekBarValue : Int, cropSwitchState : Boolean) : Int {

        if (uriString != wallpaperRepositoryImpl.getUri()) return 1
        if (seekBarValue != wallpaperRepositoryImpl.getSeekBarValue()) return 2
        if (cropSwitchState != wallpaperRepositoryImpl.getCropState()) return 3

        return 0
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
package com.ryunen344.twikot.settings.preferences.wallpaper

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

interface WallpaperPreferenceContract {

    interface View : PreBaseView<Presenter> {
        fun setSharedPreferencesParam(uriStr : String?, seekBarValue : Int, cropState : Boolean)
        fun initView()
        fun openImagePicker()
        fun showWallpaperImage(imageUri : Uri)
    }

    interface Presenter : PreBasePresenter<View> {
        fun setAbsoluteFilePath(path : String)
        fun selectWallpaperImage()
        fun loadSharedPreferences()
        fun saveCurrentPreferences(uriString : String?, seekBarValue : Int, cropSwitchState : Boolean)
        fun saveWallpaperImage(uriString : String?)
        fun result(requestCode : Int, resultCode : Int, data : Intent?)
        fun setTempBitMap(bitmap : Bitmap?)
    }

}
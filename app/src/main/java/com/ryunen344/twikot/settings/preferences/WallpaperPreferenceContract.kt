package com.ryunen344.twikot.settings.preferences

import android.content.Intent
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
        fun selectWallpaperImage()
        fun loadSharedPreferences()
        fun saveCurrentPreferences(uriString : String?, seekBarValue : Int, cropSwitchState : Boolean)
        fun result(requestCode : Int, resultCode : Int, data : Intent?)
    }

}
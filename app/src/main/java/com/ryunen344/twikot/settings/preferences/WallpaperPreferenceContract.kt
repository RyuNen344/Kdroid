package com.ryunen344.twikot.settings.preferences

import android.content.Intent
import android.net.Uri
import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

interface WallpaperPreferenceContract {

    interface View : PreBaseView<Presenter> {
        fun doSomething()
        fun openImagePicker()
        fun showWallpaperImage(imageUri : Uri)
    }

    interface Presenter : PreBasePresenter<View> {
        fun doSomething()
        fun selectWallpaperImage()
        fun result(requestCode : Int, resultCode : Int, data : Intent?)
    }

}
package com.ryunen344.kdroid.mediaViewer

import android.content.Context
import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView

interface MediaViewerContract {

    interface View : PreBaseView<Presenter> {
        fun showImage(mediaUrl: String)
    }

    interface Presenter : PreBasePresenter<View> {
        fun saveImage(context: Context)
        fun reloadImage()
        fun rotateRight()
        fun rotateLeft()
    }


}
package com.ryunen344.twikot.mediaViewer

import android.content.Context
import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

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
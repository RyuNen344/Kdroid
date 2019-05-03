package com.ryunen344.kdroid.mediaViewer

import android.content.Context
import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface MediaViewerContract {

    interface View : BaseView<Presenter> {
        fun showImage(mediaUrl: String)
    }

    interface Presenter : BasePresenter {
        fun saveImage(context: Context)
        fun reloadImage()
        fun rotateRight()
        fun rotateLeft()
    }


}
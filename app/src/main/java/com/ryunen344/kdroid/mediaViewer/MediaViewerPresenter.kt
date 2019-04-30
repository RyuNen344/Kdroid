package com.ryunen344.kdroid.mediaViewer

import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog

class MediaViewerPresenter(var mediaViewerView: MediaViewerContract.View, var appProvider: AppProvider, val mediaUrl: String) : MediaViewerContract.Presenter {

    init {
        mediaViewerView.setPresenter(this)
    }

    override fun start() {
        debugLog("start")
        mediaViewerView.showImage(mediaUrl)
        debugLog("end")
    }

    override fun saveImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reloadImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rotateRight() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rotateLeft() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
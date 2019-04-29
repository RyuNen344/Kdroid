package com.ryunen344.kdroid.mediaViewer

import com.ryunen344.kdroid.di.provider.AppProvider

class MediaViewerPresenter(var mediaViewerView: MediaViewerContract.View, var appProvider: AppProvider) : MediaViewerContract.Presenter {

    init {
        mediaViewerView.setPresenter(this)
    }

    override fun start() {
        //nop
    }

}
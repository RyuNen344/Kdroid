package com.ryunen344.twikot.settings.preferences

import com.ryunen344.twikot.util.LogUtil
import org.koin.core.KoinComponent

class WallpaperPreferencePresenter : WallpaperPreferenceContract.Presenter, KoinComponent {

    override lateinit var view : WallpaperPreferenceContract.View

    override fun doSomething() {
        LogUtil.d()
        view.doSomething()
    }

    override fun start() {
        LogUtil.d()
        view.doSomething()
    }


}
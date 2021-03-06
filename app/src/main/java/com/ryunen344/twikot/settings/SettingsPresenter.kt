package com.ryunen344.twikot.settings

import com.ryunen344.twikot.util.LogUtil
import org.koin.core.KoinComponent

class SettingsPresenter : SettingsContract.Presenter, KoinComponent {

    override lateinit var view : SettingsContract.View

    override fun start() {
        LogUtil.d()
        view.doSomething()
    }

    override fun doSomething() {
        LogUtil.d()
        view.doSomething()
    }


}
package com.ryunen344.kdroid.setting

import com.ryunen344.kdroid.util.LogUtil
import org.koin.core.KoinComponent

class SettingPresenter : SettingContract.Presenter, KoinComponent {

    override lateinit var view : SettingContract.View

    override fun start() {
        LogUtil.d()
        view.doSomething()
    }

    override fun doSomething() {
        LogUtil.d()
        view.doSomething()
    }


}
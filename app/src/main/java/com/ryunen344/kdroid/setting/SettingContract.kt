package com.ryunen344.kdroid.setting

import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView

interface SettingContract {

    interface View : PreBaseView<Presenter> {
        fun doSomething()
    }

    interface Presenter : PreBasePresenter<View> {
        fun doSomething()
    }
}
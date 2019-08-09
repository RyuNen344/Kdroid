package com.ryunen344.kdroid.settings

import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView

interface SettingsContract {

    interface View : PreBaseView<Presenter> {
        fun doSomething()
    }

    interface Presenter : PreBasePresenter<View> {
        fun doSomething()
    }
}
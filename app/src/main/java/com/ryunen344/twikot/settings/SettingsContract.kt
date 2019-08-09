package com.ryunen344.twikot.settings

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

interface SettingsContract {

    interface View : PreBaseView<Presenter> {
        fun doSomething()
    }

    interface Presenter : PreBasePresenter<View> {
        fun doSomething()
    }
}
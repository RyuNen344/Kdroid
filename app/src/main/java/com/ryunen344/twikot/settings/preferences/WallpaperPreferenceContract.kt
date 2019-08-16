package com.ryunen344.twikot.settings.preferences

import com.ryunen344.twikot.PreBasePresenter
import com.ryunen344.twikot.PreBaseView

interface WallpaperPreferenceContract {

    interface View : PreBaseView<Presenter> {
        fun doSomething()
    }

    interface Presenter : PreBasePresenter<View> {
        fun doSomething()
    }

}
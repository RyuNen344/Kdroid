package com.ryunen344.kdroid.mediaViewer

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface MediaViewerContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }


}
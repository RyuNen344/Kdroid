package com.ryunen344.kdroid.main

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface MainContract{

    interface View : BaseView<Presenter> {

        val isActive : Boolean
        fun showTitle(title : String)
        fun hideDescription()
    }

    interface Presenter : BasePresenter {
        fun editTask()
    }
}
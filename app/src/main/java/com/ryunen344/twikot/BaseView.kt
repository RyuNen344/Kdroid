package com.ryunen344.twikot

interface BaseView<T> {
    fun setPresenter(presenter:T)
    fun showError(e: Throwable)
}

interface PreBaseView<out T : PreBasePresenter<*>> {
    val presenter : T
    fun showError(e : Throwable)
}
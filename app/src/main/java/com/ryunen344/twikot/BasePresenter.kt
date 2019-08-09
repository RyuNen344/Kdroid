package com.ryunen344.twikot

interface BasePresenter{
    fun start()
}

interface PreBasePresenter<T> {
    fun start()
    var view : T
}
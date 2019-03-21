package com.ryunen344.kdroid.main

class MainPresenter(mainView : MainContract.View) : MainContract.Presenter{

    var mMainView : MainContract.View = mainView
    init {
        mMainView.setPresenter(this)
    }

    override fun editTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        mMainView.setLoadingIndicator(true)
        mMainView.showTitle("hogehoge")
        mMainView.showDescription("dhogehoge")
    }

}
package com.ryunen344.kdroid.presenter

import com.ryunen344.kdroid.contract.TimelineContract

class TimelinePresenter : TimelineContract.Presenter {
    override fun loadTweet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchTab(tab : Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        loadTweet();
    }

}
package com.ryunen344.kdroid.contract

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView

interface TimelineContract{
    interface View : BaseView<Presenter>{

        fun openTweetDetail();

        fun openTweetArea();
    }

    interface Presenter : BasePresenter {

        fun loadTweet();

        fun switchTab(tab : Int);

    }
}
package com.ryunen344.kdroid.main

import androidx.fragment.app.Fragment;

class MainFragment : Fragment() , MainContract.View{
    override val isActive : Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun showTitle(title : String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideDescription() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter : MainContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
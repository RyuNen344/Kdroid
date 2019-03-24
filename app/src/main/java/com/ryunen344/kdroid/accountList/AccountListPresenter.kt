package com.ryunen344.kdroid.accountList

import android.content.res.Resources
import android.text.TextUtils
import com.ryunen344.kdroid.R.string.*

class AccountListPresenter(val accountListView : AccountListContract.View) : AccountListContract.Presenter{

    init {
        accountListView.setPresenter(this)
    }

    override fun start() {
        //fixme
        //loadAccountList()
    }

    override fun loadAccountList() {
        accountListView.showProgress(true)
    }

    override fun addAccountWithOAuth() {
        //fixme
    }

    override fun deleteAccount() {
        //fixme
    }

}
package com.ryunen344.kdroid.accountList

import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import com.ryunen344.kdroid.data.Account

interface AccountListContract{

    interface View : BaseView<Presenter> {
        fun showAccountList(accountList : List<Account>)
        fun showNoAccount()
        fun showProgress(show : Boolean)

    }

    interface Presenter : BasePresenter {
        fun loadAccountList()
        fun addAccountWithOAuth()
        fun deleteAccount()
    }

    interface AccountItemListner{
        fun onAccountClick(clickedAccount : Account)
    }
}
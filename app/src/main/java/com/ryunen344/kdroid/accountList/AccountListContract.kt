package com.ryunen344.kdroid.accountList

import android.net.Uri
import com.ryunen344.kdroid.BasePresenter
import com.ryunen344.kdroid.BaseView
import com.ryunen344.kdroid.data.Account
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken

interface AccountListContract {

    interface View : BaseView<Presenter> {
        fun showAccountList(accountList : List<Account>)
        fun showNoAccount()
        fun showProgress(show : Boolean)
        fun showCallbak(req : RequestToken?, uri : Uri?)
        fun openAccountTimeLine(account : Account)
    }

    interface Presenter : BasePresenter {
        fun loadAccountList()
        fun addAccountWithOAuth(oauth : OAuthAuthorization, consumerKey : String, consumerSecretKey : String)
        fun deleteAccount()
    }

    interface AccountItemListner {
        fun onAccountClick(clickedAccount : Account)
    }
}
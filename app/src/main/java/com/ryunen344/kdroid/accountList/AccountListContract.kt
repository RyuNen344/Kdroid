package com.ryunen344.kdroid.accountList

import android.net.Uri
import com.ryunen344.kdroid.PreBasePresenter
import com.ryunen344.kdroid.PreBaseView
import com.ryunen344.kdroid.domain.entity.Account
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken

interface AccountListContract {

    interface View : PreBaseView<Presenter> {
        fun showAccountList(accountList : List<AccountAndAccountDetail>)
        fun showExistAccount()
        fun showNoAccount()
        fun showProgress(show : Boolean)
        fun showCallback(req : RequestToken?, uri : Uri?)
        fun showAccountHome(account : Account)
    }

    interface Presenter : PreBasePresenter<View> {
        fun loadAccountList()
        fun addAccountWithOAuth(oauth : OAuthAuthorization, consumerKey : String, consumerSecretKey : String)
        fun deleteAccount()
        fun clearDisposable()
    }

    interface AccountItemListener {
        fun onAccountClick(clickedAccount : Account)
    }
}
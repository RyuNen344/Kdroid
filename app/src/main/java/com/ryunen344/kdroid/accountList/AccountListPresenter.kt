package com.ryunen344.kdroid.accountList

import android.net.Uri
import android.os.Handler
import twitter4j.TwitterException
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import kotlin.concurrent.thread

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

    override fun addAccountWithOAuth(oauth : OAuthAuthorization, consumerKey : String, consumerSecretKey : String) {
        //fixme
        var mReq: RequestToken? = null
        // Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
        oauth.setOAuthConsumer(consumerKey, consumerSecretKey)
        oauth.oAuthAccessToken = null
        var mUri: Uri? = null

        val handler = Handler()
        thread {
            //アプリの認証オブジェクト作成
            try {
                mReq  = oauth.getOAuthRequestToken("kdroidappscheme://")
                mUri  = Uri.parse(mReq?.authorizationURL)
                // Log.i(TAG, mUri.toString())
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            handler.post {
                if (mUri != null) {
                    accountListView.showCallbak(mReq,mUri)
                } else {
                    //startActivity(ErrorActivity)
                }
            }
        }

    }

    override fun deleteAccount() {
        //fixme
    }

}
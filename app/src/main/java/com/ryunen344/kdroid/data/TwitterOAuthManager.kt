package com.ryunen344.kdroid.data

import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationContext

class TwitterOAuthManager(private val consumerKey : String, private val cosumerSecret : String){

    companion object {
        private val hoge = "hoge"
        // Oauth認証オブジェクト作成
        val mOauth = OAuthAuthorization(ConfigurationContext.getInstance())
        var mReq: RequestToken? = null

    }



}
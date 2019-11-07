package com.ryunen344.twikot.domain.repository

import android.net.Uri
import io.reactivex.Flowable
import twitter4j.auth.AccessToken
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationContext

class OAuthRepositoryImpl {

    private val oAuthAuthorization = OAuthAuthorization(ConfigurationContext.getInstance())
    private lateinit var requestToken : RequestToken

    fun loadAccessToken(verifier : String) : AccessToken =
            oAuthAuthorization.getOAuthAccessToken(requestToken, verifier)

    fun loadAuthorizationURL() : Flowable<Uri> =
            Flowable.just(Uri.parse(requestToken.authorizationURL))

    // Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
    fun initOAuthAuthorization(consumerKey : String, consumerSecretKey : String) {
        oAuthAuthorization.setOAuthConsumer(consumerKey, consumerSecretKey)
        requestToken = oAuthAuthorization.getOAuthRequestToken("kdroidappscheme://")
    }
}
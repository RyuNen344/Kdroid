package com.ryunen344.twikot.domain.repository

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import twitter4j.auth.AccessToken
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationContext


class OAuthRepositoryImpl {

    private val oAuthAuthorization = OAuthAuthorization(ConfigurationContext.getInstance())
    private lateinit var requestToken : RequestToken

    fun loadAccessToken(verifier : String) : Single<AccessToken> {
        return Single.create { emitter : SingleEmitter<AccessToken> ->
            emitter.onSuccess(oAuthAuthorization.getOAuthAccessToken(requestToken, verifier))
        }.subscribeOn(Schedulers.io())
    }

    fun loadAuthorizationURL() : Uri =
            Uri.parse(requestToken.authorizationURL)

    // Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
    fun initOAuthAuthorization(consumerKey : String, consumerSecretKey : String) : Completable {
        return Completable.create { emitter : CompletableEmitter ->
            oAuthAuthorization.setOAuthConsumer(consumerKey, consumerSecretKey)
            requestToken = oAuthAuthorization.getOAuthRequestToken("kdroidappscheme://")
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
    }
}
package com.ryunen344.kdroid.accountList

import android.net.Uri
import android.os.Handler
import com.ryunen344.kdroid.domain.repository.AccountRepositoryImpl
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.TwitterException
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AccountListPresenter : AccountListContract.Presenter, KoinComponent {

    override lateinit var view : AccountListContract.View

    private var mCompositeDisposable : CompositeDisposable = CompositeDisposable()
    private val accountRepositoryImpl : AccountRepositoryImpl by inject()

    override fun start() {
        //fixme
        LogUtil.d()
        loadAccountList()
    }

    override fun loadAccountList() {
        LogUtil.d()
        view.showProgress(true)
        val disposable = accountRepositoryImpl.findAccountList()
                .delay(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.showAccountList(it)
                        },
                        {
                            view.showError(it)
                        }
                )
        mCompositeDisposable.add(disposable)
    }

    override fun addAccountWithOAuth(oauth : OAuthAuthorization, consumerKey : String, consumerSecretKey : String) {
        LogUtil.d()
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
                view.showError(e)
            }
            handler.post {
                if (mUri != null) {
                    view.showCallback(mReq, mUri)
                } else {
                    view.showError(Throwable("uri is null"))
                }
            }
        }

    }

    override fun deleteAccount() {
        //fixme
    }

    override fun clearDisposable() {
        LogUtil.d()
        mCompositeDisposable.clear()
    }

}
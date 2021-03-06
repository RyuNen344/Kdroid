package com.ryunen344.twikot.accountList

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R.layout.activity_oauth_callback
import com.ryunen344.twikot.domain.entity.Account
import com.ryunen344.twikot.domain.repository.AccountRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import twitter4j.TwitterException
import twitter4j.auth.AccessToken
import kotlin.concurrent.thread

class OAuthCallBackActivity : AppCompatActivity() {

    private val accountRepositoryImpl : AccountRepositoryImpl by inject()

    companion object {
        var token : AccessToken? = null
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        Log.d("OAuthCallBackActivity", "onCreate Start")
        super.onCreate(savedInstanceState)
        //fixme
        setContentView(activity_oauth_callback)

        //Twitterの認証画面から発行されるIntentからUriを取得
        val uri = intent.data
        if (uri != null && uri.toString().startsWith("kdroidappscheme://")) {

            val handler = Handler()
            thread {
                //oauth_verifierを取得する
                val verifier = uri.getQueryParameter("oauth_verifier")

                try {
                    //AccessTokenオブジェクトを取得
                    token = AccountListFragment.mOauth.getOAuthAccessToken(AccountListFragment.mReq, verifier)
                } catch (e : TwitterException) {
                    LogUtil.e(e)
                }
                handler.post {
                    Log.d("OAuthCallBackActivity", "token is " + token?.token)

                    // 書き込み（永続化）
                    accountRepositoryImpl.insertAccount(Account(token!!.userId, token!!.screenName, token!!.token, token!!.tokenSecret))
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    {
                                        var intent : Intent = Intent(this, AccountListActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        overridePendingTransition(0, 0)
                                        finish()
                                    },
                                    { e ->
                                        e.printStackTrace()
                                    }
                            )
                }
            }
        } else {
            LogUtil.d("uri is unknown")
        }


    }
}

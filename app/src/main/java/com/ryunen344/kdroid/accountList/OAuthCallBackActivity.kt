package com.ryunen344.kdroid.accountList

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_oauth_callback
import kotlinx.android.synthetic.main.activity_oauth_callback.*
import twitter4j.TwitterException
import twitter4j.auth.AccessToken
import kotlin.concurrent.thread

class OAuthCallBackActivity : AppCompatActivity(){

    companion object {
        var token : AccessToken? = null
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        Log.d("OAuthCallBackActivity","onCreate Start")
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
                } catch (e: TwitterException) {
                    e.printStackTrace()
                }
                handler.post {
                    // TextView一個のレイアウト
                    callback_text.text = "token：" + token?.token + "\r\n" + "tokenSecret：" + token?.tokenSecret + "\r\n" + "screenName：" + token?.screenName + "\r\n" + "userId：" + token?.userId
                    Log.d("OAuthCallBackActivity","token is " + callback_text.text)

                    // 書き込み（永続化）
                    val spref: SharedPreferences = getDefaultSharedPreferences(this)
                    val editor = spref.edit()
                    editor.putString("token", token?.token)
                    editor.putString("tokenSecret", token?.tokenSecret)
                    editor.putString("screenName", token?.screenName)
                    editor.putLong("userId", token!!.userId)
                    editor.apply()
                }
            }
        } else {
            callback_text.text = "uri is unknown"
        }

    }



}
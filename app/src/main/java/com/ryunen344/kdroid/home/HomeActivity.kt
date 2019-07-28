package com.ryunen344.kdroid.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_home
import com.ryunen344.kdroid.R.string.consumer_key
import com.ryunen344.kdroid.R.string.consumer_secret_key
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import twitter4j.conf.ConfigurationBuilder

class HomeActivity : AppCompatActivity() {

    val appProvider : AppProvider by inject()
    val apiProvider : ApiProvider by inject()
    lateinit var mPresenter : HomeContract.Presenter

    companion object {
        const val INTENT_KEY_USER_ID : String = "key_user_id"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        intent.getLongExtra("userId", 0)

        //config mTwitter instance
        val builder : ConfigurationBuilder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(getString(consumer_key))
        builder.setOAuthConsumerSecret(getString(consumer_secret_key))
        appProvider.configureTwitter(builder)

        var homeFragment : HomeFragment? = supportFragmentManager.findFragmentById(homeFrame.id) as HomeFragment?
                ?: HomeFragment.newInstance().also {
                    replaceFragmentInActivity(supportFragmentManager, it, homeFrame.id)
                }

        mPresenter = HomePresenter(homeFragment!!, appProvider, apiProvider, intent.extras)
    }

}

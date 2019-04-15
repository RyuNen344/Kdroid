package com.ryunen344.kdroid.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_main
import com.ryunen344.kdroid.R.string.consumer_key
import com.ryunen344.kdroid.R.string.consumer_secret_key
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import twitter4j.conf.ConfigurationBuilder

class MainActivity : AppCompatActivity() {

    val appProvider : AppProvider by inject()
    val apiProvider: ApiProvider by inject()
    lateinit var mPresenter : MainContract.Presenter

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        intent.getLongExtra("userId", 0)

        val builder : ConfigurationBuilder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(getString(consumer_key))
        builder.setOAuthConsumerSecret(getString(consumer_secret_key))
        appProvider.configureTwitter(builder)

        var mainFragment : MainFragment? = supportFragmentManager.findFragmentById(mainFrame.id) as MainFragment? ?: MainFragment.newInstance().also{
            replaceFragmentInActivity(supportFragmentManager,it,mainFrame.id)
        }

        mPresenter = MainPresenter(mainFragment!!, appProvider, apiProvider, intent.getLongExtra("userId", 0))

    }

}

package com.ryunen344.kdroid.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_home
import com.ryunen344.kdroid.R.string.consumer_key
import com.ryunen344.kdroid.R.string.consumer_secret_key
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import twitter4j.conf.ConfigurationBuilder

class HomeActivity : AppCompatActivity() {

    private val appProvider : AppProvider by inject()
    private val homeFragment : HomeFragment by inject()

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

        var bundle : Bundle = Bundle()
        bundle.putLong(INTENT_KEY_USER_ID, intent.getLongExtra(INTENT_KEY_USER_ID, 0))
        homeFragment.arguments = bundle

        //config mTwitter instance
        val builder : ConfigurationBuilder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(getString(consumer_key))
        builder.setOAuthConsumerSecret(getString(consumer_secret_key))
        appProvider.configureTwitter(builder)

        supportFragmentManager.findFragmentById(homeFrame.id) as HomeFragment?
                ?: homeFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, homeFrame.id)
                }

    }

}

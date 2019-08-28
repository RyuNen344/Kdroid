package com.ryunen344.twikot.home

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R.layout.activity_home
import com.ryunen344.twikot.R.string.consumer_key
import com.ryunen344.twikot.R.string.consumer_secret_key
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.domain.repository.WallpaperRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import twitter4j.conf.ConfigurationBuilder

class HomeActivity : AppCompatActivity() {

    private val appProvider : AppProvider by inject()
    private val homeFragment : HomeFragment by inject()
    private val wallpaperRepositoryImpl : WallpaperRepositoryImpl by inject()

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

    override fun onResume() {
        LogUtil.d()
        super.onResume()
        wallpaperRepositoryImpl.getUri()?.let {
            coordinatorLayout.background = BitmapDrawable(resources, it)
            coordinatorLayout.background.alpha = 80
        }
    }

}

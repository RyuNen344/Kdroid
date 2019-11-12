package com.ryunen344.twikot.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ryunen344.twikot.R
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.util.LogUtil
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
        setContentView(R.layout.activity_home)

        val bundle = Bundle()
        bundle.putLong(INTENT_KEY_USER_ID, intent.getLongExtra(INTENT_KEY_USER_ID, 0))
        homeFragment.arguments = bundle

        //config mTwitter instance
        val builder : ConfigurationBuilder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(getString(R.string.consumer_key))
        builder.setOAuthConsumerSecret(getString(R.string.consumer_secret_key))
        appProvider.configureTwitter(builder)

        supportFragmentManager.commit {
            replace(homeContainer.id, homeFragment)
        }
    }

//    override fun onResume() {
//        LogUtil.d()
//        super.onResume()
//        wallpaperRepositoryImpl.getUri()?.let {
//            coordinatorLayout.background = BitmapDrawable(resources, it)
//            coordinatorLayout.background.alpha = 80
//        }
//    }

}

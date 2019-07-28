package com.ryunen344.kdroid.tweetDetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.activity_tweet_detail.*
import org.koin.android.ext.android.inject

class TweetDetailActivity : AppCompatActivity() {

    private val tweetDetailFragment : TweetDetailFragment by inject()

    companion object {
        const val INTENT_KEY_TWEET_ID : String = "key_tweet_id"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        var bundle : Bundle = Bundle()
        bundle.putLong(INTENT_KEY_TWEET_ID, intent.getLongExtra(INTENT_KEY_TWEET_ID, 0))

        tweetDetailFragment.arguments = bundle

        supportFragmentManager.findFragmentById(tweetDetailFrame.id) as TweetDetailFragment?
                ?: tweetDetailFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, tweetDetailFrame.id)
                }

    }

    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        LogUtil.d()

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        LogUtil.d()
        when (item.itemId) {
            android.R.id.home -> {
                LogUtil.d("back button pressed")
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
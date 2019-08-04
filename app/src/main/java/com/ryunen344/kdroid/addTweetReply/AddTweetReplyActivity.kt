package com.ryunen344.kdroid.addTweetReply

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_add_tweet_reply
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_add_tweet_reply.*
import org.koin.android.ext.android.inject

class AddTweetReplyActivity : AppCompatActivity() {

    private val appProvider : AppProvider by inject()
    private val addTweetReplyFragment : AddTweetReplyFragment by inject()

    companion object {
        const val REQUEST_ADD_TWEET : Int = 10
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_add_tweet_reply)
        //setSupportActionBar(toolbar_add_tweet_reply)
        //supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //supportActionBar?.setDisplayShowHomeEnabled(true)

        supportFragmentManager.findFragmentById(addTweetReplyFrame.id) as AddTweetReplyFragment?
                ?: addTweetReplyFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, addTweetReplyFrame.id)
                }
    }

    override fun onSupportNavigateUp() : Boolean {
        onBackPressed()
        return true
    }

}
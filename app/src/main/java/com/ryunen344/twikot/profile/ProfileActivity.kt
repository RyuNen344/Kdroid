package com.ryunen344.twikot.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : AppCompatActivity() {

    private val profileFragment : ProfileFragment by inject()

    companion object {
        const val INTENT_KEY_USER_ID : String = "key_user_id"
        const val INTENT_KEY_SCREEN_NAME: String = "key_screen_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        var bundle : Bundle = Bundle()
        bundle.putLong(INTENT_KEY_USER_ID, intent.getLongExtra(INTENT_KEY_USER_ID, 0))
        bundle.putString(INTENT_KEY_SCREEN_NAME, intent.getStringExtra(INTENT_KEY_SCREEN_NAME))
        profileFragment.arguments = bundle

        supportFragmentManager.findFragmentById(profileFrame.id) as ProfileFragment?
                ?: profileFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, profileFrame.id)
                }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        LogUtil.d()

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

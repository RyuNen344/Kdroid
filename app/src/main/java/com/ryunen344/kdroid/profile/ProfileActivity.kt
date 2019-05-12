package com.ryunen344.kdroid.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : AppCompatActivity() {

    val appProvider: AppProvider by inject()
    val apiProvider: ApiProvider by inject()
    lateinit var mPresenter: ProfileContract.Presenter

    companion object {
        val INTENT_KEY_USER_ID: String = "key_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var profileFragment: ProfileFragment? = supportFragmentManager.findFragmentById(profileFrame.id) as ProfileFragment?
                ?: ProfileFragment.newInstance().also {
                    it.arguments = intent.extras
                    replaceFragmentInActivity(supportFragmentManager, it, profileFrame.id)
                }

        mPresenter = ProfilePresenter(profileFragment!!, appProvider, apiProvider, intent.getLongExtra(INTENT_KEY_USER_ID, 0))
        debugLog("end")


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        debugLog("start")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile, menu)
        debugLog("end")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        debugLog("start")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.wrap) {
            return true
        }
        debugLog("end")
        return super.onOptionsItemSelected(item)
    }
}

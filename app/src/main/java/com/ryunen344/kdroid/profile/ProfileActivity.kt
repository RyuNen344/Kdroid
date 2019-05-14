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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject
import twitter4j.User

class ProfileActivity : AppCompatActivity() {


    val appProvider: AppProvider by inject()
    val apiProvider: ApiProvider by inject()
    lateinit var mPresenter: ProfileContract.Presenter
    private var mPicasso: Picasso = Picasso.get()


    private var infoListener: ProfileContract.ProfileInfoListener = object : ProfileContract.ProfileInfoListener {
        override fun showUserInfo(user: User) {
            debugLog("start")

            debugLog(user.screenName)
            debugLog(user.name)
            debugLog(user.description)
            profile_screen_name.text = user.screenName
            profile_description.text = user.description
            profile_place.text = user.name

            mPicasso
                    .load(user.profileBanner1500x500URL)
                    .placeholder(R.drawable.ic_loading_image_24dp)
                    .error(R.drawable.ic_loading_image_24dp)
                    .into(profile_banner)

            mPicasso
                    .load(user.originalProfileImageURLHttps)
                    .placeholder(R.drawable.ic_loading_image_24dp)
                    .error(R.drawable.ic_loading_image_24dp)
                    .into(profile_icon)

            debugLog("start")
        }
    }

    companion object {
        val INTENT_KEY_USER_ID: String = "key_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        debugLog("start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        var profileFragment: ProfileFragment? = supportFragmentManager.findFragmentById(profileFrame.id) as ProfileFragment?
                ?: ProfileFragment.newInstance().also {
                    it.arguments = intent.extras
                    replaceFragmentInActivity(supportFragmentManager, it, profileFrame.id)
                }

        mPresenter = ProfilePresenter(profileFragment!!, appProvider, apiProvider, intent.getLongExtra(INTENT_KEY_USER_ID, 0), infoListener)
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
        when (item.itemId) {
            android.R.id.home -> {
                debugLog("back button pressed")
                finish()
            }
        }
        debugLog("end")
        return super.onOptionsItemSelected(item)
    }


}

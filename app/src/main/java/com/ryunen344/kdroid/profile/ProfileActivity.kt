package com.ryunen344.kdroid.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.ApiProvider
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject
import twitter4j.User

class ProfileActivity : AppCompatActivity() {


    val appProvider: AppProvider by inject()
    val apiProvider: ApiProvider by inject()
    lateinit var mPresenter: ProfileContract.Presenter
    private var mPicasso: Picasso = appProvider.providePiccaso()


    private var infoListener: ProfileContract.ProfileInfoListener = object : ProfileContract.ProfileInfoListener {
        override fun showUserInfo(user: User) {
            LogUtil.d()
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
        }
    }

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

        var profileFragment: ProfileFragment? = supportFragmentManager.findFragmentById(profileFrame.id) as ProfileFragment?
                ?: ProfileFragment.newInstance().also {
                    replaceFragmentInActivity(supportFragmentManager, it, profileFrame.id)
                }

        mPresenter = ProfilePresenter(profileFragment!!, appProvider, apiProvider, intent.extras, infoListener)
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

package com.ryunen344.twikot.settings.preferences.license

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R.layout.activity_license
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_license.*
import org.koin.android.ext.android.inject

class LicenseActivity : AppCompatActivity() {

    private val licenseFragment : LicenseFragment by inject()

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_license)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        var bundle = Bundle()
        licenseFragment.arguments = bundle

        supportFragmentManager.findFragmentById(licenseFrame.id) as LicenseFragment?
                ?: licenseFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, licenseFrame.id)
                }

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
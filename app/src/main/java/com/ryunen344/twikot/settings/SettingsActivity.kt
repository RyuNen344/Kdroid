package com.ryunen344.twikot.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.twikot.R.layout.activity_setting
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_setting.*
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {

    private val settingsFragment : SettingsFragment by inject()

    companion object {
        const val REQUEST_SETTING : Int = 13
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        supportFragmentManager.findFragmentById(settingsFrame.id) as SettingsFragment?
                ?: settingsFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, settingsFrame.id)
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

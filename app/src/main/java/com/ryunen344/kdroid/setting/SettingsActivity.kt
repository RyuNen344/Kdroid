package com.ryunen344.kdroid.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_setting
import com.ryunen344.kdroid.util.LogUtil
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_setting.*
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {

    private val settingFragment : SettingFragment by inject()

    companion object {
        const val REQUEST_SETTING : Int = 13
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        setContentView(activity_setting)

        supportFragmentManager.findFragmentById(settingsFrame.id) as SettingFragment?
                ?: settingFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, settingsFrame.id)
                }
    }


}

package com.ryunen344.kdroid.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.activity_main
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.addFragmentToActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val appProvider : AppProvider by inject()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        var mainFragment : MainFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as MainFragment?
        // fragmentがnullだったらnewInstance
        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance()
            addFragmentToActivity(supportFragmentManager,mainFragment,R.id.contentFrame)
        }
        MainPresenter(mainFragment!!,appProvider)


    }

}

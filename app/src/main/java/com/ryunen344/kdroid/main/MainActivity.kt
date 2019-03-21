package com.ryunen344.kdroid.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.R.layout.activity_main
import com.ryunen344.kdroid.util.addFragmentToActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        // fragmentがnullだったらnewInstance
        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            addFragmentToActivity(supportFragmentManager,mainFragment,R.id.contentFrame)
        }
    }

}

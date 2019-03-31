package com.ryunen344.kdroid.main

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_main
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val appProvider : AppProvider by inject()
    lateinit var mPresenter : MainContract.Presenter

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        var mainFragment : MainFragment? = supportFragmentManager.findFragmentById(mainFrame.id) as MainFragment? ?: MainFragment.newInstance().also{
            replaceFragmentInActivity(supportFragmentManager,it,mainFrame.id)
        }

        mPresenter = MainPresenter(mainFragment!!,appProvider, PreferenceManager.getDefaultSharedPreferences(this))

    }

}

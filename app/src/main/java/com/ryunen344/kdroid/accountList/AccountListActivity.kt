package com.ryunen344.kdroid.accountList

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_account_lsit
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_account_lsit.*


class AccountListActivity : AppCompatActivity() {
    lateinit var mPresenter : AccountListContract.Presenter

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_account_lsit)

        val accountListFragment : AccountListFragment = supportFragmentManager.findFragmentById(accountListFrame.id) as AccountListFragment? ?: AccountListFragment.newInstance().also{
            replaceFragmentInActivity(supportFragmentManager,it,accountListFrame.id)
        }

        mPresenter = AccountListPresenter(accountListFragment)

    }
}

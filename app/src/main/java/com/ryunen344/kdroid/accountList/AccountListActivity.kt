package com.ryunen344.kdroid.accountList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryunen344.kdroid.R.layout.activity_account_lsit
import com.ryunen344.kdroid.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_account_lsit.*
import org.koin.android.ext.android.inject


class AccountListActivity : AppCompatActivity() {

    private val accountListFragment : AccountListFragment by inject()

    companion object {
        const val INTENT_KEY_USER_ID : String = "key_user_id"
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_account_lsit)

        supportFragmentManager.findFragmentById(accountListFrame.id) as AccountListFragment?
                ?: accountListFragment.also {
                    replaceFragmentInActivity(supportFragmentManager, it, accountListFrame.id)
                }

    }
}

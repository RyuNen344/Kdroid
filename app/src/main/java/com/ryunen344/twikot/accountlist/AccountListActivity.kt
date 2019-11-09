package com.ryunen344.twikot.accountlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ryunen344.twikot.R
import kotlinx.android.synthetic.main.activity_account_lsit.*
import org.koin.android.ext.android.inject


class AccountListActivity : AppCompatActivity() {

    private val accountListFragment : AccountListFragment by inject()

    companion object {
        const val INTENT_KEY_USER_ID : String = "key_user_id"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_lsit)

        supportFragmentManager.commit {
            replace(accountListContainer.id, accountListFragment)
        }
    }
}

package com.ryunen344.twikot.accountList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.twikot.IOState
import com.ryunen344.twikot.R.layout.fragment_account_list
import com.ryunen344.twikot.R.string.consumer_key
import com.ryunen344.twikot.R.string.consumer_secret_key
import com.ryunen344.twikot.domain.entity.Account
import com.ryunen344.twikot.errorMessage
import com.ryunen344.twikot.home.HomeActivity
import com.ryunen344.twikot.util.LogUtil
import kotlinx.android.synthetic.main.activity_account_lsit.*
import kotlinx.android.synthetic.main.fragment_account_list.*
import kotlinx.android.synthetic.main.fragment_account_list.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class AccountListFragment : Fragment() {

    private val accountListViewModel : AccountListViewModel by viewModel()

    private lateinit var noAccountListView : View
    private lateinit var noAccountIconView : ImageView
    private lateinit var noAccountListMainView : TextView
    private lateinit var noAccountListAddView : TextView
    private lateinit var accountListView : LinearLayout
    private lateinit var mLayoutManager : LinearLayoutManager
    private lateinit var mRecyclerView : RecyclerView

    private var itemListener : AccountListContract.AccountItemListener = object : AccountListContract.AccountItemListener {
        override fun onAccountClick(clickedAccount : Account) {
            //fixme
            LogUtil.d("open timeline of ${clickedAccount.screenName}")
            showAccountHome(clickedAccount)
        }
    }

    private var accountListAdapter = AccountListAdapter(ArrayList(0), itemListener)

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)

        accountListViewModel.ioState.observe(this, Observer { ioState ->
            when (ioState) {
                is IOState.NOPE -> return@Observer
                is IOState.LOADING -> LogUtil.d("loading")
                is IOState.LOADED -> LogUtil.d("load finish, hide seek bar")
                is IOState.ERROR -> LogUtil.d(ioState.error.createMessage { errorMessage(it) })
            }
        })

        accountListViewModel.oAuthRequestUri.observe(this, Observer { uri ->
            if (uri != null) {
                sshowCallback(uri)
            }
        })
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(fragment_account_list, container, false)
        with(root) {
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerView = account_list.apply {
                this.layoutManager = mLayoutManager
                this.setHasFixedSize(true)
                this.adapter = accountListAdapter
                this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }

            accountListView = accountLL

            noAccountListView = noAccountList
            noAccountIconView = noAccountIcon
            noAccountListMainView = noAccountListMain
            noAccountListAddView = noAccountListAdd

        }

        activity?.account_fab?.setOnClickListener {
            accountListViewModel.generateOAuthRequestUri(getString(consumer_key), getString(consumer_secret_key))
        }

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        account_list.adapter = accountListAdapter
        accountListViewModel.loadAccountList()
    }

    override fun onResume() {
        LogUtil.d()
        super.onResume()
    }

    override fun onDestroy() {
        LogUtil.d()
        super.onDestroy()
    }

    fun sshowCallback(uri : Uri?) {
        LogUtil.d()
        startActivityForResult(Intent(Intent.ACTION_VIEW, uri), 0)
    }

    fun showAccountHome(account : Account) {
        LogUtil.d()
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(AccountListActivity.INTENT_KEY_USER_ID, account.userId)
        startActivity(intent)
    }
}
package com.ryunen344.kdroid.accountList

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R.layout.fragment_account_list
import com.ryunen344.kdroid.R.string.consumer_key
import com.ryunen344.kdroid.R.string.consumer_secret_key
import com.ryunen344.kdroid.domain.entity.Account
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import com.ryunen344.kdroid.home.HomeActivity
import com.ryunen344.kdroid.util.LogUtil
import kotlinx.android.synthetic.main.activity_account_lsit.*
import kotlinx.android.synthetic.main.fragment_account_list.*
import kotlinx.android.synthetic.main.fragment_account_list.view.*
import org.koin.android.scope.currentScope
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationContext


class AccountListFragment : Fragment(), AccountListContract.View {

    override val presenter : AccountListContract.Presenter by currentScope.inject()

    private lateinit var noAccountListView : View
    private lateinit var noAccountIconView : ImageView
    private lateinit var noAccountListMainView : TextView
    private lateinit var noAccountListAddView : TextView
    private lateinit var accountListView : LinearLayout
    private lateinit var mLayoutManager : LinearLayoutManager
    private lateinit var mRecyclerView : RecyclerView

    companion object {
        fun newInstance() = AccountListFragment()
        var mOauth = OAuthAuthorization(ConfigurationContext.getInstance())
        var mReq : RequestToken? = null
    }

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
        presenter.view = this
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
            presenter.addAccountWithOAuth(mOauth, getString(consumer_key), getString(consumer_secret_key))
        }

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        account_list.adapter = accountListAdapter
    }

    override fun onResume() {
        LogUtil.d()
        super.onResume()
        presenter.start()
    }

    override fun onDestroy() {
        LogUtil.d()
        presenter.clearDisposable()
        super.onDestroy()
    }

    override fun showAccountList(accountList : List<AccountAndAccountDetail>) {
        LogUtil.d("accountList size is ${accountList.size}")
        showProgress(false)
        accountListAdapter.accountList = accountList
        if (accountList.isNotEmpty()) {
            showExistAccount()
        } else {
            showNoAccount()
        }
        accountListAdapter.notifyDataSetChanged()
    }

    override fun showExistAccount() {
        LogUtil.d()
        accountListView.visibility = View.VISIBLE
        noAccountListView.visibility = View.GONE
    }

    override fun showNoAccount() {
        LogUtil.d()
        accountListView.visibility = View.GONE
        noAccountListView.visibility = View.VISIBLE
    }

    override fun showProgress(show : Boolean) {
        LogUtil.d("show Progressive Bar $show")
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        activity?.loading_progress?.visibility = if (show) View.VISIBLE else View.GONE
        activity?.coordinatorLayout?.visibility = if (show) View.GONE else View.VISIBLE
        activity?.accountListFrame?.visibility = if (show) View.GONE else View.VISIBLE
        activity?.account_fab?.visibility = if (show) View.GONE else View.VISIBLE
        activity?.loading_progress?.animate()
                ?.setDuration(shortAnimTime)
                ?.alpha((if (show) 1 else 0).toFloat())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation : Animator) {
                        activity?.loading_progress?.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun showCallback(req : RequestToken?, uri : Uri?) {
        LogUtil.d()
        mReq = req
        startActivityForResult(Intent(Intent.ACTION_VIEW, uri), 0)
    }


    override fun showAccountHome(account : Account) {
        LogUtil.d()
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(AccountListActivity.INTENT_KEY_USER_ID, account.userId)
        startActivity(intent)
    }

    override fun showError(e : Throwable) {
        Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

}
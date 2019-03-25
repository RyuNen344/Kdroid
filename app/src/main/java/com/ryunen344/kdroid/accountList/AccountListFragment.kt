package com.ryunen344.kdroid.accountList

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ryunen344.kdroid.R.layout.fragment_account_list
import com.ryunen344.kdroid.R.string.consumer_key
import com.ryunen344.kdroid.R.string.consumer_secret_key
import com.ryunen344.kdroid.data.Account
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_account_lsit.*
import kotlinx.android.synthetic.main.fragment_account_list.view.*
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationContext


class AccountListFragment : Fragment() , AccountListContract.View{

    lateinit var mPresenter : AccountListContract.Presenter
    private lateinit var noAccountListView: View
    private lateinit var noAccountIconView: ImageView
    private lateinit var noAccountListMainView: TextView
    private lateinit var noAccountListAddView: TextView
    lateinit var filteringLabelView : TextView
    lateinit var accountListView : LinearLayout


    companion object {
        fun newInstance() =  AccountListFragment()
        var mOauth = OAuthAuthorization(ConfigurationContext.getInstance())
        var mReq: RequestToken? = null
    }

    private var itemListener: AccountListContract.AccountItemListner = object : AccountListContract.AccountItemListner {
        override fun onAccountClick(clickedAccount : Account) {
            //fixme
            //mPresenter.openAccountTimeLine()
            println("open timeline")
        }
    }

    private val accountListAdapter = AccountListAdapter(ArrayList(0), itemListener)

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        var root : View = inflater.inflate(fragment_account_list,container,false)
        with(root){
            val listView = account_list.apply {
                adapter = accountListAdapter
            }

            filteringLabelView = filteringLabel
            accountListView = accountLL

            noAccountListView = noAccountList
            noAccountIconView = noAccountIcon
            noAccountListMainView =noAccountListMain
            noAccountListAddView = noAccountListAdd

            activity?.account_fab?.setOnClickListener {
                mPresenter.addAccountWithOAuth(mOauth,getString(consumer_key),getString(consumer_secret_key))
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun showAccountList(accountList : List<Account>) {
        accountListAdapter.accountList = accountList
        accountListView.visibility = View.VISIBLE
        noAccountListView.visibility = View.GONE

    }

    override fun showNoAccount() {
        accountListView.visibility = View.GONE
        noAccountListView.visibility = View.VISIBLE
    }

    override fun showProgress(show : Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation : Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun showCallbak(req: RequestToken? , uri : Uri?) {
        mReq = req
        startActivityForResult(Intent(Intent.ACTION_VIEW, uri), 0)
    }


    override fun setPresenter(presenter : AccountListContract.Presenter) {
        ensureNotNull(presenter){ p ->
            mPresenter = p
        }
    }

}
package com.ryunen344.twikot.accountlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ryunen344.twikot.IOState
import com.ryunen344.twikot.R
import com.ryunen344.twikot.databinding.FragmentAccountListBinding
import com.ryunen344.twikot.errorMessage
import com.ryunen344.twikot.home.HomeActivity
import com.ryunen344.twikot.util.LogUtil
import org.koin.android.viewmodel.ext.android.viewModel


class AccountListFragment : Fragment() {

    private lateinit var binding : FragmentAccountListBinding
    private val accountListViewModel : AccountListViewModel by viewModel()

    private var itemListener : AccountItemListener = object : AccountItemListener {
        override fun onAccountClick(clickedUserId : Long) {
            //fixme
            LogUtil.d("open timeline of $clickedUserId")
            showAccountHome(clickedUserId)
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
    ) : View? {
        LogUtil.d()

        binding = DataBindingUtil.inflate<FragmentAccountListBinding>(
                inflater,
                R.layout.fragment_account_list,
                container,
                false
        ).also {
            it.lifecycleOwner = this@AccountListFragment.viewLifecycleOwner
            it.viewModel = accountListViewModel
        }

//      account_fab?.setOnClickListener {
//          accountListViewModel.generateOAuthRequestUri(getString(consumer_key), getString(consumer_secret_key))
//      }
        accountListViewModel.ioState.observe(this@AccountListFragment.viewLifecycleOwner, Observer { ioState ->
            when (ioState) {
                is IOState.NOPE -> return@Observer
                is IOState.LOADING -> LogUtil.d("loading")
                is IOState.LOADED -> LogUtil.d("load finish, hide seek bar")
                is IOState.ERROR -> LogUtil.d(ioState.error.createMessage { errorMessage(it) })
            }
        })

        accountListViewModel.oAuthRequestUri.observe(this@AccountListFragment.viewLifecycleOwner, Observer { uri ->
            if (uri != null) {
                showCallback(uri)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        accountListViewModel.loadAccountList()
    }

    private fun showCallback(uri : Uri?) {
        LogUtil.d()
        startActivityForResult(Intent(Intent.ACTION_VIEW, uri), 0)
    }

    fun showAccountHome(userId : Long) {
        LogUtil.d()
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(AccountListActivity.INTENT_KEY_USER_ID, userId)
        startActivity(intent)
    }
}
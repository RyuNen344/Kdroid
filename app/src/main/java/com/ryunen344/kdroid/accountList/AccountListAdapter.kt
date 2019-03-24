package com.ryunen344.kdroid.accountList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ryunen344.kdroid.R.layout.fragment_account_list
import com.ryunen344.kdroid.data.Account

class AccountListAdapter(accountList : List<Account>,val accountItemListner : AccountListContract.AccountItemListner) : BaseAdapter(){

    var accountList : List<Account> = accountList
    set(accountList: List<Account>){
        field = accountList
        notifyDataSetChanged()
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val account : Account = getItem(position)
        val rowView = convertView ?: LayoutInflater.from(parent.context)
                .inflate(fragment_account_list, parent, false)
        rowView.setOnClickListener{accountItemListner.onAccountClick(account)}
        return rowView
    }

    override fun getItem(position : Int) : Account {
        return accountList[position]
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun getCount() : Int {
        return accountList.size
    }

}
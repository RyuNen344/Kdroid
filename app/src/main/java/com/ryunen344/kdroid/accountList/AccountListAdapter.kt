package com.ryunen344.kdroid.accountList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ryunen344.kdroid.R.id.account_user_name
import com.ryunen344.kdroid.R.layout.fragment_account_list
import com.ryunen344.kdroid.R.layout.item_account_list
import com.ryunen344.kdroid.data.Account
import kotlinx.android.synthetic.main.item_account_list.view.*

class AccountListAdapter(accountList : List<Account>,val accountItemListner : AccountListContract.AccountItemListner) : BaseAdapter(){

    var accountList : List<Account> = accountList
    set(accountList: List<Account>){
        field = accountList
        notifyDataSetChanged()
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val account : Account = getItem(position)
        var view : View? = convertView
        var holder : ViewHolder

        if(view == null){
            view = LayoutInflater.from(parent.context).inflate(item_account_list, parent, false)
            holder = ViewHolder(view.account_icon,view.account_user_name,view.account_user_id)
            view.tag = holder
        }else{
            holder = view.tag as ViewHolder
        }

        holder.account_user_id.text = account.userId.toString()
        holder.account_user_name.text = account.screenName
//        val rowView = convertView ?: LayoutInflater.from(parent.context)
//                .inflate(fragment_account_list, parent, false)
        view!!.setOnClickListener{accountItemListner.onAccountClick(account)}
        return view!!
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

    data class ViewHolder(var account_icon: ImageView,var account_user_name: TextView, var account_user_id: TextView)
}

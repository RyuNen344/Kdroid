package com.ryunen344.kdroid.accountList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.ryunen344.kdroid.R.layout.item_account_list
import com.ryunen344.kdroid.data.Account
import com.ryunen344.kdroid.util.debugLog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_account_list.view.*


class AccountListAdapter(accountList: List<Account>, val accountItemListner: AccountListContract.AccountItemListner) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    var accountList : List<Account> = accountList
        set(accountList : List<Account>) {
            field = accountList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view: View = LayoutInflater.from(parent.context).inflate(item_account_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //fixme
        //holder.account_icon(accountList[position].)
        holder.account_user_id.text = accountList[position].userId.toString()
        holder.account_user_name.text = accountList[position].screenName
        mCompositeDisposable.add(holder.itemView.clicks()
                //.throttleFirst(3, TimeUnit.SECONDS)
                .take(1)
                .subscribe {
                    accountItemListner.onAccountClick(accountList[position])
                    debugLog("subscribe on account click")
                })
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var account_icon: ImageView = itemView.account_icon
        var account_user_name: TextView = itemView.account_user_name
        var account_user_id: TextView = itemView.account_user_id

    }
}

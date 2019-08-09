package com.ryunen344.kdroid.accountList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.ryunen344.kdroid.R.layout.item_account_list
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import com.ryunen344.kdroid.util.LogUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_account_list.view.*
import java.io.File


class AccountListAdapter(accountList : List<AccountAndAccountDetail>, private val accountItemListener : AccountListContract.AccountItemListener) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    var accountList : List<AccountAndAccountDetail> = accountList
        set(accountList : List<AccountAndAccountDetail>) {
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

        if (accountList[position].accountDetails.isNotEmpty()) {
            if (File(accountList[position].accountDetails[0].localProfileImage).exists()) {
                holder.account_icon.setImageURI(File(accountList[position].accountDetails[0].localProfileImage).toUri())
            }

            accountList[position].accountDetails[0].userName.let {
                holder.account_screen_name.text = it
            }

        }

        holder.account_user_name.text = accountList[position].account.screenName

        mCompositeDisposable.add(holder.itemView.clicks()
                //.throttleFirst(3, TimeUnit.SECONDS)
                .take(1)
                .subscribe {
                    accountItemListener.onAccountClick(accountList[position].account)
                    LogUtil.d("subscribe on account click")
                })

    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var account_icon: ImageView = itemView.account_icon
        var account_user_name: TextView = itemView.account_user_name
        var account_screen_name : TextView = itemView.account_screen_name

    }
}

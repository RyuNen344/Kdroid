package com.ryunen344.twikot.accountlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.ryunen344.twikot.R
import com.ryunen344.twikot.databinding.ItemAccountListBinding
import com.ryunen344.twikot.domain.entity.AccountAndAccountDetail
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.disposables.CompositeDisposable


class AccountListAdapter : ListAdapter<AccountAndAccountDetail, AccountListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<AccountAndAccountDetail>() {
            override fun areItemsTheSame(oldItem : AccountAndAccountDetail, newItem : AccountAndAccountDetail) : Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem : AccountAndAccountDetail, newItem : AccountAndAccountDetail) : Boolean {

                if (oldItem.account.userId == newItem.account.userId
                        && oldItem.account.screenName == newItem.account.screenName
                ) return true
                return false
            }

        }
) {

    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    private var _clickedUserId : MutableLiveData<Long> = MutableLiveData()
    val clickedUserId : LiveData<Long>
        get() = _clickedUserId

    override fun getItemViewType(position : Int) : Int = R.layout.item_account_list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        viewType,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        (holder.binding as ItemAccountListBinding).item = getItem(position)
        holder.itemView.clicks()
                .take(1)
                .subscribe {
                    LogUtil.d("subscribe on account click")
                    _clickedUserId.value = getItem(position).account.userId
                }.let { compositeDisposable.add(it) }

        holder.binding.executePendingBindings()
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    class ViewHolder(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}

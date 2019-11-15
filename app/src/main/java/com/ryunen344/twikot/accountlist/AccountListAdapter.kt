package com.ryunen344.twikot.accountlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.twikot.R
import com.ryunen344.twikot.databinding.ItemAccountListBinding
import com.ryunen344.twikot.entity.Account
import com.ryunen344.twikot.util.LogUtil
import com.ryunen344.twikot.util.throttledClicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class AccountListAdapter : ListAdapter<Account, AccountListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem : Account, newItem : Account) : Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem : Account, newItem : Account) : Boolean {

                if (oldItem.userId == newItem.userId
                        && oldItem.screenName == newItem.screenName
                        && oldItem.token == newItem.token
                        && oldItem.tokenSecret == newItem.tokenSecret
                        && oldItem.profileImage == newItem.profileImage
                        && oldItem.localProfileImage == newItem.localProfileImage
                ) return true
                return false
            }
        }
) {

    private val disposable = CompositeDisposable()
    var lifecycleOwner : LifecycleOwner? = null

    private var _clickedUserId : MutableLiveData<Long> = MutableLiveData()
    val clickedUserId : LiveData<Long>
        get() = _clickedUserId

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_account_list,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        LogUtil.d()

        (holder.binding as ItemAccountListBinding).item = getItem(position)
        holder.itemView.throttledClicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _clickedUserId.value = getItem(position).userId
                }
                .addTo(disposable)
        lifecycleOwner?.let { it ->
            holder.binding.lifecycleOwner = it
        }

        holder.binding.executePendingBindings()
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun onDetachedFromRecyclerView(recyclerView : RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposable.clear()
    }

    class ViewHolder(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}
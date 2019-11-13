package com.ryunen344.twikot.accountlist

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.twikot.entity.AccountAndAccountDetail
import com.ryunen344.twikot.util.LogUtil

class AccountListRecyclerView @JvmOverloads constructor(
        context : Context, attrs : AttributeSet? = null, defStyleAttr : Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val adapter = AccountListAdapter()

    init {
        setAdapter(adapter)
    }

    fun setItems(items : List<AccountAndAccountDetail>?) {
        LogUtil.d("items size is ${items?.size}")
        adapter.submitList(items)
    }
}
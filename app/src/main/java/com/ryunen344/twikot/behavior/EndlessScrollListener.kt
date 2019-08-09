package com.ryunen344.twikot.behavior

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(private val layoutManager : LinearLayoutManager) : RecyclerView.OnScrollListener() {

    var firstVisibleItem : Int = 0
    var lastVisibleItem : Int = 0
    var visibleItemCount : Int = 0
    var totalItemCount : Int = 0

    private var visibleThreshold : Int = 5

    private var previousTotal : Int = 0
    private var loading : Boolean = true
    private var current_page : Int = 1

    override fun onScrolled(recyclerView : RecyclerView, dx : Int, dy : Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) < (firstVisibleItem + visibleThreshold)) {
            current_page++
            onLoadMore(current_page)
            loading = true
        }
    }

    abstract fun onLoadMore(currentPage : Int)

}
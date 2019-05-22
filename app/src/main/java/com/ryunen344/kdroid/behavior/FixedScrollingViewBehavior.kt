package com.ryunen344.kdroid.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout


class FixedScrollingViewBehavior(context : Context?, attrs : AttributeSet? = null) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    override fun onMeasureChild(parent : CoordinatorLayout, child : View, parentWidthMeasureSpec : Int, widthUsed : Int, parentHeightMeasureSpec : Int, heightUsed : Int) : Boolean {
        if (child.layoutParams.height == -1) {
            val dependencies = parent.getDependencies(child)
            if (dependencies.isEmpty()) {
                return false
            }

            val appBar = findFirstAppBarLayout(dependencies)
            if (appBar != null && ViewCompat.isLaidOut(appBar)) {
                if (ViewCompat.getFitsSystemWindows(appBar)) {
                    child.fitsSystemWindows = true
                }

                val scrollRange = appBar.totalScrollRange
                val height = parent.height - appBar.measuredHeight + Math.min(scrollRange, parent.height - heightUsed)
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
                return true
            }
        }

        return false
    }

    private fun findFirstAppBarLayout(views : List<View>) : AppBarLayout? {
        var i = 0

        val z = views.size
        while (i < z) {
            val view = views[i]
            if (view is AppBarLayout) {
                return view
            }
            ++i
        }

        return null
    }

}
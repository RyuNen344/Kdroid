package com.ryunen344.kdroid

import android.util.AttributeSet
import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class BottomNavigationBehavior(context : Context, attrs : AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    private var isSnackbarShowing = false
    private var snackbar : Snackbar.SnackbarLayout? = null

    override fun layoutDependsOn(parent : CoordinatorLayout, child : BottomNavigationView, dependency : View) : Boolean {
        return dependency is AppBarLayout || dependency is Snackbar.SnackbarLayout
    }

    override fun onStartNestedScroll(coordinatorLayout : CoordinatorLayout, child : BottomNavigationView, directTargetChild : View, target : View, nestedScrollAxes : Int) : Boolean {
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout : CoordinatorLayout, child : BottomNavigationView, target : View, dx : Int, dy : Int, consumed : IntArray) {
        if (isSnackbarShowing) {
            if (snackbar != null) {
                updateSnackbarPaddingByBottomNavigationView(child)
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
    }

    override fun onDependentViewChanged(parent : CoordinatorLayout, child : BottomNavigationView, dependency : View) : Boolean {
        if (dependency is AppBarLayout) {
            val appbar : AppBarLayout = dependency as AppBarLayout
            val bottom : Float = appbar.bottom.toFloat()
            val height :Float = appbar.height.toFloat()
            val hidingRate : Float = (height - bottom) / height
            child.translationY = child.height * hidingRate
            return true
        }
        if (dependency is Snackbar.SnackbarLayout) {
            if (isSnackbarShowing) return true
            isSnackbarShowing = true
            snackbar = dependency
            updateSnackbarPaddingByBottomNavigationView(child)
            return true
        }
        return false
    }

    override fun onDependentViewRemoved(parent : CoordinatorLayout, child : BottomNavigationView, dependency : View) {
        if (dependency is Snackbar.SnackbarLayout) {
            isSnackbarShowing = false
            snackbar = null
        }
        super.onDependentViewRemoved(parent, child, dependency)
    }

    private fun updateSnackbarPaddingByBottomNavigationView(view : BottomNavigationView) {
        if (snackbar != null) {
            val bottomTranslate = (view.height - view.translationY).toInt()
            snackbar!!.setPadding(snackbar!!.paddingLeft, snackbar!!.paddingTop, snackbar!!.paddingRight, bottomTranslate)
            snackbar!!.requestLayout()
        }
    }

    companion object {
        private val TAG = "CustomBehavior"
    }
}
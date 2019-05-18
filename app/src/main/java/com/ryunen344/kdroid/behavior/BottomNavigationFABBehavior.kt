package com.ryunen344.kdroid.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class BottomNavigationFABBehavior<V : View>(context : Context?, attrs : AttributeSet? = null) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent : CoordinatorLayout, child : V, dependency : View) : Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewRemoved(parent : CoordinatorLayout, child : V, dependency : View) {
        child.translationY = 0.0F
    }

    override fun onDependentViewChanged(parent : CoordinatorLayout, child : V, dependency : View) : Boolean {
        return this.updateButton(child, dependency)
    }

    private fun updateButton(child : View, dependency : View) : Boolean {
        return if (dependency is Snackbar.SnackbarLayout) {
            val oldTranslation : Float = child.translationY
            val height : Float = dependency.height.toFloat()
            val newTranslation : Float = dependency.translationY - height
            child.translationY = newTranslation
            oldTranslation != newTranslation
        } else {
            false
        }
    }
}
package com.ryunen344.kdroid.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

inline fun <T> ensureNotNull(p1: T?, f: (p1: T) -> Unit){
    if (p1 != null) f(p1)
}

fun replaceFragmentInActivity(fragmentManager : FragmentManager,fragment: Fragment, @IdRes frameId: Int) {
    fragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun addFragmentToActivity(fragmentManager : FragmentManager,fragment : Fragment, @IdRes frameId : Int){
    fragmentManager.transact {
        add(frameId,fragment)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}
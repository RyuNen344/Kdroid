package com.ryunen344.kdroid.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ryunen344.kdroid.main.MainContract

inline fun <T> ensureNotNull(p1: T?, f: (p1: T) -> Unit){
    if (p1 != null) f(p1)
}

fun addFragmentToActivity(fragmentManager : FragmentManager,fragment : Fragment,frameId : Int){
    val transaction : FragmentTransaction = fragmentManager.beginTransaction()
    transaction.add(frameId,fragment)
    transaction.commit()
}
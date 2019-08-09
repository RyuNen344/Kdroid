package com.ryunen344.twikot.util

import android.os.Environment
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

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

/* Checks if external storage is available for read and write */
fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

/* Checks if external storage is available to at least read */
fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}

fun splitLastThreeWord(string : String) : String {
    val list = string.split("/".toRegex())
    return list[list.size - 3] + "-" + list[list.size - 2] + "-" + list[list.size - 1]
}


package com.ryunen344.kdroid.util

import android.util.Log
import com.ryunen344.kdroid.BuildConfig
import java.util.regex.Pattern

fun debugLog(msg : String) {
    if (!BuildConfig.DEBUG) return
    Log.d(getTag(), msg)
}

fun errorLog(msg : String) {
    Log.e(getTag(), msg)
}

fun errorLog(msg : String, t : Throwable) {
    Log.e(getTag(), msg, t)
}

private fun getTag() : String {
    val trace : StackTraceElement = Thread.currentThread().stackTrace[4]
    val cla : String = trace.className
    var pattern : Pattern = Pattern.compile("[\\.]")
    val splitedStr : List<String> = pattern.split(cla).toList()
    val simpleClass : String = splitedStr[splitedStr.size - 1]

    val mthd : String = trace.methodName
    val line : Int = trace.lineNumber
    val tag : String = "$simpleClass#$mthd:$line"
    return tag
}

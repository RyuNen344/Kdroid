package com.ryunen344.twikot.util

import android.util.Log
import com.ryunen344.twikot.BuildConfig
import java.util.regex.Pattern

object LogUtil {

    fun v() {
        Log.v(getTag(), getMessage(null))
    }

    fun v(msg : String) {
        Log.v(getTag(), getMessage(msg))
    }

    fun v(any : Any) {
        Log.v(getTag(), getMessage(any.toString()))
    }

    fun v(msg : String, t : Throwable) {
        Log.v(getTag(), getMessage(msg), t)
    }

    fun v(t : Throwable) {
        Log.v(getTag(), getMessage(t.message), t)
    }

    fun i() {
        Log.i(getTag(), getMessage(null))
    }

    fun i(msg : String) {
        Log.i(getTag(), getMessage(msg))
    }

    fun i(any : Any) {
        Log.v(getTag(), getMessage(any.toString()))
    }

    fun i(msg : String, t : Throwable) {
        Log.i(getTag(), getMessage(msg), t)
    }

    fun i(t : Throwable) {
        Log.i(getTag(), getMessage(t.message), t)
    }

    fun d() {
        if (!BuildConfig.DEBUG) return
        Log.d(getTag(), getMessage(null))
    }

    fun d(msg : String?) {
        if (!BuildConfig.DEBUG) return
        Log.d(getTag(), getMessage(msg))
    }

    fun d(any : Any?) {
        if (!BuildConfig.DEBUG) return
        Log.d(getTag(), getMessage(any.toString()))
    }

    fun d(t : Throwable) {
        if (!BuildConfig.DEBUG) return
        Log.d(getTag(), getMessage(t.message), t)
    }

    fun d(msg : String?, t : Throwable) {
        if (!BuildConfig.DEBUG) return
        Log.d(getTag(), getMessage(msg), t)
    }

    fun e() {
        Log.e(getTag(), getMessage(null))
    }

    fun e(msg : String) {
        Log.e(getTag(), getMessage(msg))
    }

    fun e(any : Any) {
        Log.e(getTag(), getMessage(any.toString()))
    }

    fun e(t : Throwable) {
        Log.e(getTag(), getMessage(t.message), t)
    }

    fun e(msg : String, t : Throwable) {
        Log.e(getTag(), getMessage(msg), t)
    }

    fun w() {
        Log.w(getTag(), getMessage(null))
    }

    fun w(msg : String) {
        Log.w(getTag(), getMessage(msg))
    }

    fun w(any : Any) {
        Log.w(getTag(), getMessage(any.toString()))
    }

    fun w(t : Throwable) {
        Log.w(getTag(), getMessage(t.message), t)
    }

    fun w(msg : String, t : Throwable) {
        Log.w(getTag(), getMessage(msg), t)
    }

    private fun getTag() : String {
        val trace : StackTraceElement = Thread.currentThread().stackTrace[4]
        val threadName = Thread.currentThread().name
        val cla : String = trace.className
        var pattern : Pattern = Pattern.compile("[\\.]")
        val splitStr : List<String> = pattern.split(cla).toList()

        return "${splitStr[splitStr.size - 1]}#${trace.methodName}:${trace.lineNumber} on {$threadName}"
    }

    private fun getMessage(msg : String?) : String {
        return msg ?: "called"
    }
}

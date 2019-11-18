package com.ryunen344.twikot.util

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun View.throttledClicks() : Observable<Unit> {
    return this.clicks().throttleFirst(1500, TimeUnit.MILLISECONDS)
}

fun View.throttledLongClicks() : Observable<Unit> {
    return this.longClicks().throttleFirst(1500, TimeUnit.MILLISECONDS)
}
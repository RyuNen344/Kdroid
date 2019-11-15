package com.ryunen344.twikot

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.ryunen344.twikot.util.LogUtil

class LoggingLifecycleObserver : LifecycleEventObserver {
    override fun onStateChanged(source : LifecycleOwner, event : Lifecycle.Event) {
        LogUtil.d("lifecycleOwner is $source : $event ")
    }
}
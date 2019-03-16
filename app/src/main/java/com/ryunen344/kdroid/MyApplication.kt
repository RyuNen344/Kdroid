package com.ryunen344.kdroid

import android.app.Application
import android.util.Log

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("TAG","onCreate::MyApplication\n");
    }
}

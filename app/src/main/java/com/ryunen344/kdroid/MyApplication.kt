package com.ryunen344.kdroid

import android.app.Application
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.module.ApiModule
import com.ryunen344.kdroid.di.module.AppModule
import com.ryunen344.kdroid.di.module.UtilModule
import com.ryunen344.kdroid.util.debugLog
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        debugLog("start")

        //init db
        AccountDatabase.init(this)

        //dependency inject
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            androidFileProperties()
            modules(listOf(AppModule, UtilModule, ApiModule))
        }

        debugLog("end")
    }

}

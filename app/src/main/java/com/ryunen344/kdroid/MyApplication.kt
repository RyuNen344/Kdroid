package com.ryunen344.kdroid

import android.app.Application
import com.ryunen344.kdroid.data.db.AccountDatabase
import com.ryunen344.kdroid.di.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //db初期化
        AccountDatabase.init(this)

        //di注入
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            androidFileProperties()
            modules(AppModule)
        }
    }

}

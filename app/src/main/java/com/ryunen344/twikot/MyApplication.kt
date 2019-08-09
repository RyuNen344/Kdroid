package com.ryunen344.twikot

import android.app.Application
import com.ryunen344.twikot.di.module.ApiModule
import com.ryunen344.twikot.di.module.AppModule
import com.ryunen344.twikot.di.module.RepositoryModule
import com.ryunen344.twikot.di.module.UtilModule
import com.ryunen344.twikot.domain.database.AccountDatabase
import com.ryunen344.twikot.util.LogUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtil.d()

        //init db
        AccountDatabase.init(this)

        //dependency inject
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            androidFileProperties()
            modules(listOf(AppModule, UtilModule, ApiModule, RepositoryModule))
        }

    }

}

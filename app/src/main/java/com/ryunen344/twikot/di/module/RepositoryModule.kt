package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.db.AccountDatabase
import com.ryunen344.twikot.repository.AccountRepositoryImpl
import com.ryunen344.twikot.repository.OAuthRepositoryImpl
import com.ryunen344.twikot.repository.TwitterMediaRepositoryImpl
import com.ryunen344.twikot.repository.TwitterRepositoryImpl
import com.ryunen344.twikot.repository.WallpaperRepositoryImpl
import org.koin.dsl.module

val RepositoryModule = module {
    single { TwitterRepositoryImpl() }
    single { TwitterMediaRepositoryImpl() }
    single { OAuthRepositoryImpl() }
    single { AccountRepositoryImpl(AccountDatabase.getInstance().accountDao()) }
    single { WallpaperRepositoryImpl(get()) }
}
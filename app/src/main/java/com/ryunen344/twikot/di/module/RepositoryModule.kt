package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.domain.repository.AccountRepositoryImpl
import com.ryunen344.twikot.domain.repository.TwitterMediaRepositoryImpl
import com.ryunen344.twikot.domain.repository.TwitterRepositoryImpl
import com.ryunen344.twikot.domain.repository.WallpaperRepositoryImpl
import org.koin.dsl.module

val RepositoryModule = module {
    single { TwitterRepositoryImpl() }
    single { TwitterMediaRepositoryImpl() }
    single { AccountRepositoryImpl() }
    single { WallpaperRepositoryImpl(get()) }
}
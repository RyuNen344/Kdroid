package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.domain.repository.TwitterRepositoryImpl
import org.koin.dsl.module

val RepositoryModule = module {
    single { TwitterRepositoryImpl() }
}
package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.di.provider.AppProvider
import org.koin.dsl.module

val AppModule = module {
    single {AppProvider()}
}
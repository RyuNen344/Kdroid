package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.di.provider.ApiProvider
import org.koin.dsl.module

val ApiModule = module {
    single { ApiProvider() }
}
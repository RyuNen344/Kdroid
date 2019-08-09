package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.di.provider.ApiProvider
import org.koin.dsl.module

val ApiModule = module {
    single { ApiProvider() }
}
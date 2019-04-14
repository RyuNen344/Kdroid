package com.ryunen344.kdroid.di.module

import com.ryunen344.kdroid.di.provider.UtilProvider
import org.koin.dsl.module

val UtilModule = module {
    single { UtilProvider() }
}
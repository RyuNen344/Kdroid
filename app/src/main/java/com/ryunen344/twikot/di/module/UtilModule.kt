package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.di.provider.UtilProvider
import org.koin.dsl.module

val UtilModule = module {
    single { UtilProvider() }
}
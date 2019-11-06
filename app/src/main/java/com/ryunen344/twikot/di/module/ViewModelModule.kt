package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.accountList.AccountListViewModel
import com.ryunen344.twikot.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountListViewModel(get()) }
    viewModel { HomeViewModel() }
}
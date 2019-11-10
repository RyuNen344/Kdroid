package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.accountlist.AccountListViewModel
import com.ryunen344.twikot.accountlist.OAuthCallBackViewModel
import com.ryunen344.twikot.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { AccountListViewModel(get(), get()) }
    viewModel { OAuthCallBackViewModel(get(), get()) }
    viewModel { HomeViewModel() }
}
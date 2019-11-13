package com.ryunen344.twikot.repository

import com.ryunen344.twikot.entity.Account
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {

    fun findAccountList() : Single<List<Account>>

    fun findAccountById(id : Long) : Single<Account>

    fun insertAccount(account : Account) : Completable
}
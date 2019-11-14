package com.ryunen344.twikot.repository

import com.ryunen344.twikot.dao.AccountDao
import com.ryunen344.twikot.entity.Account
import io.reactivex.Completable
import io.reactivex.Single

class AccountRepositoryImpl(private val accountDao : AccountDao) : AccountRepository {

    override fun findAccountList() : Single<List<Account>> = accountDao.findAccountList()

    override fun findAccountById(id : Long) : Single<Account> = accountDao.loadAccountById(id)

    override fun insertAccount(account : Account) : Completable = accountDao.insertAccount(account)

}
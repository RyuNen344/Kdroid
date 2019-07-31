package com.ryunen344.kdroid.domain.repository

import com.ryunen344.kdroid.domain.database.AccountDatabase
import com.ryunen344.kdroid.domain.entity.Account
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import com.ryunen344.kdroid.domain.entity.AccountDetail
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class AccountRepositoryImpl : AccountRepository {

    private var accountDatabase : AccountDatabase? = AccountDatabase.getInstance()
    private lateinit var accountDao : AccountRepository

    init {
        accountDatabase?.let { it ->
            accountDao = it.accountRepository()
        }
    }

    fun provideDatabase() : AccountDatabase {
        accountDatabase?.let { it ->
            accountDao = it.accountRepository()
        }
        return accountDatabase!!
    }

    override fun findAccountList() : Single<List<AccountAndAccountDetail>> {
        return accountDao.findAccountList()
    }

    override fun loadAccountById(id : Long) : Flowable<AccountAndAccountDetail> {
        return accountDao.loadAccountById(id)
    }

    override fun insertAccount(account : Account) : Completable {
        return accountDao.insertAccount(account)
    }

    override fun deleteAccount(account : Account) : Completable {
        return accountDao.deleteAccount(account)
    }

    override fun insertAccountDetail(accountDetail : AccountDetail) : Completable {
        return accountDao.insertAccountDetail(accountDetail)
    }

}
package com.ryunen344.kdroid.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ryunen344.kdroid.domain.entity.Account
import com.ryunen344.kdroid.domain.entity.AccountAndAccountDetail
import com.ryunen344.kdroid.domain.entity.AccountDetail
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AccountDao{

    @Query("select * from Account")
    fun findAccountList() : Single<List<AccountAndAccountDetail>>

    @Query("SELECT * from Account where userId = :id LIMIT 1")
    fun loadAccountById(id : Long) : Flowable<AccountAndAccountDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account : Account) : Completable

    @Delete
    fun deleteAccount(account : Account) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccountDetail(accountDetail : AccountDetail) : Completable
}
package com.ryunen344.twikot.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ryunen344.twikot.entity.Account
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AccountDao {

    @Query("select * from Account")
    fun findAccountList() : Single<List<Account>>

    @Query("SELECT * from Account where userId = :id")
    fun loadAccountById(id : Long) : Single<Account>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAccount(account : Account) : Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccount(account : Account) : Completable

    @Delete
    fun deleteAccount(account : Account) : Completable

}
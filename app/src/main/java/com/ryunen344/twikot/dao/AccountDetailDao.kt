package com.ryunen344.twikot.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ryunen344.twikot.entity.AccountAndAccountDetail
import com.ryunen344.twikot.entity.AccountDetail
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AccountDetailDao {

    @Query(value = " select * from Account left join AccountDetail on Account.userId = AccountDetail.userId")
    fun findAccountDetailList() : Single<List<AccountAndAccountDetail>>

    @Query("SELECT * from Account left join AccountDetail on Account.userId = AccountDetail.userId where Account.userId = :id")
    fun loadAccountDetailById(id : Long) : Single<AccountAndAccountDetail>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAccountDetail(accountDetail : AccountDetail) : Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccountDetail(accountDetail : AccountDetail) : Completable

    @Delete
    fun deleteAccountDetail(accountDetail : AccountDetail) : Completable
}
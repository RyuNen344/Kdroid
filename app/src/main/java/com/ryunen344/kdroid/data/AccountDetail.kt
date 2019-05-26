package com.ryunen344.kdroid.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "AccountDetail",
        foreignKeys = arrayOf(ForeignKey(
                entity = Account::class,
                parentColumns = arrayOf("userId"),
                childColumns = arrayOf("userId"),
                onDelete = ForeignKey.CASCADE
        )))
data class AccountDetail(
        @PrimaryKey(autoGenerate = false)
        val userId : Long,
        val userName : String?,
        val profileImage : String?,
        val profileBannerImage : String?
)
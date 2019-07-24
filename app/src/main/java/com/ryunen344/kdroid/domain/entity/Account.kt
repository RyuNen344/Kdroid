package com.ryunen344.kdroid.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class Account(
        @PrimaryKey(autoGenerate = false)
        val userId : Long,
        val screenName : String,
        val token : String,
        val tokenSecret : String
)
package com.ryunen344.kdroid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
        @PrimaryKey(autoGenerate = false)
        val userId : Long?,
        val screenName : String?,
        val token : String?,
        val tokenSecret : String?
)
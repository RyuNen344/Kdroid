package com.ryunen344.kdroid.data

import androidx.room.Embedded
import androidx.room.Relation

class AccountAndAccountDetail {
    @Embedded
    lateinit var account : Account

    @Relation(parentColumn = "userId", entityColumn = "userId")
    lateinit var accountDetails : List<AccountDetail>
}
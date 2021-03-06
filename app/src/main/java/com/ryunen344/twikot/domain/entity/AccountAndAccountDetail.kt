package com.ryunen344.twikot.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

class AccountAndAccountDetail {
    @Embedded
    lateinit var account : Account

    @Relation(parentColumn = "userId", entityColumn = "userId")
    lateinit var accountDetails : List<AccountDetail>
}
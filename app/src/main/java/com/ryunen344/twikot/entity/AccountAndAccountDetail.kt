package com.ryunen344.twikot.entity

import androidx.room.Embedded
import androidx.room.Relation

class AccountAndAccountDetail {
    @Embedded
    lateinit var account : Account

    @Relation(parentColumn = "userId", entityColumn = "userId", entity = AccountDetail::class)
    var accountDetail : AccountDetail? = null
}
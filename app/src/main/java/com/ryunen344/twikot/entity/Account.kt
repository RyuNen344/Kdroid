package com.ryunen344.twikot.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class Account(
        @PrimaryKey(autoGenerate = false)
        val userId : Long,
        val screenName : String,
        val token : String,
        val tokenSecret : String,
        val userName : String?,
        val profileImage : String?,
        val localProfileImage : String?,
        val profileBannerImage : String?,
        val localProfileBannerImage : String?
) {
    constructor(userId : Long, screenName : String, token : String, tokenSecret : String) : this(userId, screenName, token, tokenSecret, null, null, null, null, null)
}
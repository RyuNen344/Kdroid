package com.ryunen344.twikot.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ryunen344.twikot.dao.AccountDao
import com.ryunen344.twikot.entity.Account

@Database(entities = [Account::class], version = AccountDatabase.DATABASE_VERSION)
abstract class AccountDatabase : RoomDatabase(){

    abstract fun accountDao() : AccountDao

    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "account.db"
        @Volatile
        private lateinit var instance : AccountDatabase

        fun init(context : Context){
            Room.databaseBuilder(context, AccountDatabase::class.java, DATABASE_NAME).build().also { instance = it }
        }

        fun getInstance() = instance
    }

}
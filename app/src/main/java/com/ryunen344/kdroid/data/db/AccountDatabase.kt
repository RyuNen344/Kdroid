package com.ryunen344.kdroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ryunen344.kdroid.data.Account
import com.ryunen344.kdroid.data.dao.AccountDao

@Database(entities = [Account::class],version = AccountDatabase.DATABASE_VERSION)
abstract class AccountDatabase : RoomDatabase(){

    abstract fun accountDao() : AccountDao

    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "account.db"
        private var instance : AccountDatabase? = null

        fun init(context : Context){
            Room.databaseBuilder(context,AccountDatabase::class.java, DATABASE_NAME).build().also { instance = it }
        }

        fun getInstance() = instance
    }

}
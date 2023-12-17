package com.hgm.secretaccountapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
      entities = [AccountEntity::class],
      version = 1
)
abstract class AccountDatabase : RoomDatabase() {

      abstract fun getDao(): AccountDao

      companion object{
            const val DATABASE_NAME="accounts_db"
      }
}
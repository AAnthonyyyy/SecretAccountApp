package com.hgm.secretaccountapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountDao {

      @Insert
      suspend fun addAccount(account: AccountEntity)

      @Query("SELECT * FROM account")
      fun getAccounts():Flow<List<AccountEntity>>

      @Delete
      suspend fun deleteAccount(account: AccountEntity)
}
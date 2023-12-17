package com.hgm.secretaccountapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
      @PrimaryKey(autoGenerate = true)
      val id: Int? = null,
      val type: String,
      val account: String,
      val password: String
)
package com.hgm.secretaccountapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hgm.secretaccountapp.db.AccountDatabase
import com.hgm.secretaccountapp.db.AccountEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
      private val db:AccountDatabase
) : ViewModel() {

      private val _accounts = mutableStateOf<List<AccountEntity>>(emptyList())
      val accounts: State<List<AccountEntity>> = _accounts

      init {
            getAllAccounts()
      }

      fun addAccount(
            type: String,
            account: String,
            password: String
      ) {
            viewModelScope.launch {
                  db.getDao().addAccount(
                        AccountEntity(
                              type=type,
                              account=account,
                              password=password
                        )
                  )
            }
      }


      fun deleteAccount(account: AccountEntity) {
            viewModelScope.launch {
                  db.getDao().deleteAccount(account)
            }
      }

      private fun getAllAccounts() {
            viewModelScope.launch {
                  db.getDao().getAccounts().collect { result ->
                        _accounts.value = result
                  }
            }
      }
}
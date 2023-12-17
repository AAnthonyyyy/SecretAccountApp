package com.hgm.secretaccountapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.hgm.secretaccountapp.ui.theme.SecretAccountAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 *    构建秘密账户应用，使用生物识别解锁应用
 **/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                  SecretAccountAppTheme {
                        Surface(
                              modifier = Modifier.fillMaxSize(),
                              color = MaterialTheme.colorScheme.background
                        ) {
                              AccountScreen()
                        }
                  }
            }
      }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountScreen(
      viewModel: MainViewModel = hiltViewModel()
) {
      val activity = LocalContext.current as AppCompatActivity
      // 是否显示账号输入框
      var showDialog by remember {
            mutableStateOf(false)
      }
      // 身份验证的状态
      val authorized = remember {
            mutableStateOf(false)
      }
      val authorize: () -> Unit = {
            BiometricHelper.showBiometricPrompt(activity) {
                  authorized.value = true
            }
      }

      // 声明周期监听
      OnLifecycleEvent { owner, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                  authorized.value = false
            }
            //if (event == Lifecycle.Event.ON_RESUME) {
            //      authorize()
            //}
      }

      // 模糊值
      val blurValue by animateDpAsState(
            targetValue = if (authorized.value) 0.dp else 15.dp,
            animationSpec = tween(500)
      )

      LaunchedEffect(true) {
            //delay(1000)
            authorize()
      }


      Scaffold(
            floatingActionButton = {
                  Column {
                        FloatingActionButton(onClick = {
                              showDialog = true
                        }) {
                              Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                              )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        FloatingActionButton(onClick = {
                              authorize()
                        }) {
                              Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null
                              )
                        }
                  }
            }
      ) { innerPadding ->
            Box(
                  modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
            ) {
                  LazyColumn(
                        modifier = Modifier
                              .fillMaxSize()
                              .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                  ) {
                        items(viewModel.accounts.value) {
                              Box(
                                    modifier = Modifier
                                          .animateItemPlacement(tween(500))
                                          .fillMaxWidth()
                                          .clip(RoundedCornerShape(8.dp))
                                          .background(MaterialTheme.colorScheme.primary)
                                          .padding(16.dp)
                                          .blur(
                                                radius = blurValue,
                                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                                          )
                              ) {
                                    Row(
                                          modifier = Modifier.fillMaxWidth(),
                                          horizontalArrangement = Arrangement.SpaceBetween,
                                          verticalAlignment = Alignment.CenterVertically
                                    ) {
                                          Column {
                                                Text(
                                                      text = it.type,
                                                      fontSize = 20.sp,
                                                      color = Color.White
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Text(
                                                      text = "账号：${it.account}",
                                                      fontSize = 15.sp,
                                                      color = Color.White
                                                )
                                                Text(
                                                      text = "密码：${it.password}",
                                                      fontSize = 15.sp,
                                                      color = Color.White
                                                )
                                          }
                                          Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                modifier = Modifier.clickable {
                                                      viewModel.deleteAccount(
                                                            it
                                                      )
                                                },
                                                tint = Color.White
                                          )
                                    }
                              }
                        }
                  }
            }

            if (showDialog) {
                  Dialog(
                        onDismissRequest = { showDialog = false }
                  ) {
                        Card(
                              modifier = Modifier.fillMaxWidth(),
                        ) {
                              Column(
                                    modifier = Modifier
                                          .fillMaxWidth()
                                          .clip(RoundedCornerShape(12.dp))
                                          .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                              ) {
                                    var type by remember { mutableStateOf("") }
                                    var account by remember { mutableStateOf("") }
                                    var password by remember { mutableStateOf("") }

                                    OutlinedTextField(
                                          value = type,
                                          onValueChange = { type = it },
                                          modifier = Modifier.fillMaxWidth(),
                                          label = { Text(text = "账号类型") },
                                          leadingIcon = {
                                                Icon(
                                                      imageVector = Icons.Default.Lock,
                                                      contentDescription = null
                                                )
                                          }
                                    )
                                    OutlinedTextField(
                                          value = account,
                                          onValueChange = { account = it },
                                          modifier = Modifier.fillMaxWidth(),
                                          label = { Text(text = "账号") },
                                          leadingIcon = {
                                                Icon(
                                                      imageVector = Icons.Default.AccountCircle,
                                                      contentDescription = null
                                                )
                                          }
                                    )
                                    OutlinedTextField(
                                          value = password,
                                          onValueChange = { password = it },
                                          modifier = Modifier.fillMaxWidth(),
                                          label = { Text(text = "密码") },
                                          leadingIcon = {
                                                Icon(
                                                      imageVector = Icons.Default.Lock,
                                                      contentDescription = null
                                                )
                                          }
                                    )
                                    Button(
                                          onClick = {
                                                viewModel.addAccount(type, account, password)
                                                showDialog = false
                                          },
                                          modifier = Modifier.fillMaxWidth(),
                                          shape = RoundedCornerShape(12.dp)
                                    ) {
                                          Text(text = "添加")
                                    }
                              }
                        }
                  }
            }
      }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
      val eventHandler = rememberUpdatedState(newValue = onEvent)
      val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

      DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { owner, event ->
                  eventHandler.value(owner, event)
            }
            lifecycle.addObserver(observer)

            onDispose {
                  lifecycle.removeObserver(observer)
            }
      }
}
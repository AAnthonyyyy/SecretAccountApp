package com.hgm.secretaccountapp

import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL


object BiometricHelper {

      /** 创建提示信息  **/
      private fun createPromptInfo(): BiometricPrompt.PromptInfo =
            BiometricPrompt.PromptInfo.Builder()
                  .setTitle("SecretAccountApp")
                  .setDescription("使用你的指纹或者面部来验证你的身份")
                  .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
                  .build()
      //setConfirmationRequired(true)
      //setNegativeButtonText("取消")


      /** 创建生物识别提示  **/
      fun showBiometricPrompt(
            activity: AppCompatActivity,
            onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
      ) {
            val executor = ContextCompat.getMainExecutor(activity)
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                  override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                  ) {
                        super.onAuthenticationError(errorCode, errString)
                        // 处理身份验证错误
                        Log.e("HGM", "onAuthenticationError: $errString")
                  }

                  override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // 处理身份验证成功
                        onSuccess(result)
                  }

                  override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // 处理身份验证失败
                        Log.e("HGM", "onAuthenticationFailed: 验证失败")
                  }
            }

            return BiometricPrompt(activity, executor, callback).authenticate(createPromptInfo())
      }
}
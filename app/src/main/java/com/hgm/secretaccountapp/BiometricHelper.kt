package com.hgm.secretaccountapp

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

/**
 * @author：HGM
 * @created：2023/12/15 0015
 * @description：
 **/
object BiometricHelper {

      /** 创建生物识别提示  **/
      private fun createBiometricPrompt(
            activity: AppCompatActivity,
            processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
      ): BiometricPrompt {
            val executor = ContextCompat.getMainExecutor(activity)

            // 传递成功的回调出去
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                  override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                  }

                  override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        processSuccess(result)
                  }

                  override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                  }
            }
            return BiometricPrompt(activity, executor, callback)
      }

      /** 创建提示信息  **/
      private fun createPromptInfo(): BiometricPrompt.PromptInfo =
            BiometricPrompt.PromptInfo.Builder()
                  .apply {
                        setTitle("SecretAccount App")
                        setDescription("使用你的指纹或者面部来解锁应用")
                        setConfirmationRequired(true)
                        setNegativeButtonText("取消")
                  }.build()


      /** 显示验证框  **/
      fun showPrompt(activity: AppCompatActivity, onSuccess: () -> Unit) {
            createBiometricPrompt(activity = activity) {
                  onSuccess()
            }
                  .authenticate(createPromptInfo())
      }
}
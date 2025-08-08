package com.vinayak.semicolon.securefolderhidefiles.Other

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class BiometricHelper(
    private val activity: FragmentActivity,
    private val onSuccess: () -> Unit,
    private val onError: (String) -> Unit
) {

    fun showBiometricCustomDialog() {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_fingerprint)
        dialog.setCancelable(false)

        val btnUnlock = dialog.findViewById<TextView>(R.id.btnUnlock)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnClose)

        btnUnlock.setOnClickListener {
            dialog.dismiss()
            showSystemBiometricPrompt()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
            onError("Authentication cancelled")
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    private fun showSystemBiometricPrompt() {
        val biometricManager = BiometricManager.from(activity)
        if (biometricManager.canAuthenticate(BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_SUCCESS) {
            onError("Biometric not available or not enrolled.")
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Authentication")
            .setSubtitle("Use fingerprint to unlock")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onError("Authentication failed")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}


//class BiometricHelper(
//    private val activity: FragmentActivity,
//    private val onSuccess: () -> Unit,
//    private val onError: (String) -> Unit
//) {
//
//    fun authenticate() {
//        val biometricManager = BiometricManager.from(activity)
//        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> {
//                showPrompt()
//            }
//            else -> {
//                onError("Biometric not supported or not enrolled")
//            }
//        }
//    }
//
//    private fun showPrompt() {
//        val executor = ContextCompat.getMainExecutor(activity)
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Unlock with Fingerprint")
//            .setSubtitle("Use your fingerprint to continue")
//            .setNegativeButtonText("Cancel")
//            .build()
//
//        val biometricPrompt = BiometricPrompt(activity, executor,
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    onSuccess()
//                }
//
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                    onError(errString.toString())
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    onError("Authentication failed")
//                }
//            })
//
//        biometricPrompt.authenticate(promptInfo)
//    }
//}
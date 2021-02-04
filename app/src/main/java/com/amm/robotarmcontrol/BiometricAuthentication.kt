package com.amm.robotarmcontrol

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.amm.robotarmcontrol.Constants.Constants
import com.amm.robotarmcontrol.Utilities.Utilities
import java.util.concurrent.Executor

class BiometricAuthentication : AppCompatActivity() {
    private lateinit var activityContext: Context
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_authentication)
        activityContext = this
        executor = ContextCompat.getMainExecutor(activityContext)
        setPrompt()
        if (Utilities.isBiometricHardWareAvailable(activityContext)) {
            initBiometricPrompt(
                    Constants.BIOMETRIC_AUTHENTICATION,
                    Constants.BIOMETRIC_AUTHENTICATION_SUBTITLE,
                    Constants.BIOMETRIC_AUTHENTICATION_DESCRIPTION,
                    false
            )
        }

    }

    private fun setPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                     override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Utilities.showSnackBar(
                                Constants.AUTHENTICATION_ERROR + " " + errString,
                                activityContext as BiometricAuthentication
                        )
                    }

                    override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        Utilities.showSnackBar(
                                Constants.AUTHENTICATION_SUCCEEDED,
                                activityContext as BiometricAuthentication
                        )
                        var moveToBTScan = Intent(applicationContext,BTScanActivity::class.java)
                        startActivity(moveToBTScan)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Utilities.showSnackBar(
                                Constants.AUTHENTICATION_FAILED,
                                activityContext as BiometricAuthentication
                        )
                    }
                })
    }

    private fun initBiometricPrompt(
            title: String,
            subtitle: String,
            description: String,
            setDeviceCred: Boolean
    ) {
        if (setDeviceCred) {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setDeviceCredentialAllowed(true)
                    .build()
        } else {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setNegativeButtonText(Constants.CANCEL)
                    .build()
        }

        findViewById<ImageButton>(R.id.fpAuthButton).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}
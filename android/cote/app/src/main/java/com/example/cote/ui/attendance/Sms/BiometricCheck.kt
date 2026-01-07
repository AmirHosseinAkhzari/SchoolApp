package com.example.cote.ui.attendance.Sms

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController



@Composable
fun BiometricChecker(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel = hiltViewModel<SmsSenderViewModel>()
    val context = LocalContext.current

    val canAuthenticate = viewModel.canAuthenticate(context)

    val activity = context as FragmentActivity

    LaunchedEffect(canAuthenticate) {
        if (canAuthenticate == true) {
            showBiometric(activity ,
                {navController.navigate("main")} ,
                {navController.navigate("mainAstin")}
            )
        }
    }
}




fun showBiometric(
    activity: FragmentActivity,
    onSuccess: () -> Unit ,
    onError : () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)

    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                Log.d("Bio", "done")
                onSuccess()
            }

            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {

                onError()
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }


        }

    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("احراز هویت")
        .setSubtitle("برای ادامه اثر انگشت یا رمز وارد کن")
        .setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        .build()

    biometricPrompt.authenticate(promptInfo)
}

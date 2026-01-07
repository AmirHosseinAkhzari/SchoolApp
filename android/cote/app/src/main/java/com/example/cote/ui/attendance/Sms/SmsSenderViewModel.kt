package com.example.cote.ui.attendance.Sms

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.ResSendSms
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SmsSenderViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {



    fun canAuthenticate(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }


    suspend fun SendSms(token: String): Result<ResSendSms?> {
        return repo.SendSms(token)
    }


}
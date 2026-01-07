package com.example.cote.ui.attendance.Sms

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.ViewModel
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


}
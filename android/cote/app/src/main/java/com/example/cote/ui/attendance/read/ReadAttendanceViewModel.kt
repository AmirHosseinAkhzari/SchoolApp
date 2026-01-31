package com.example.cote.ui.attendance.read

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.ResAttendanceChangeStatus
import com.example.cote.data.remote.ResReadAttendance
import com.example.cote.data.remote.ResSendSms
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReadAttendanceViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {





    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }


    suspend fun ReadAttendance(token: String , date : String): Result<ResReadAttendance?> {
        return repo.ReadAttendance(token , date)
    }

    suspend fun ChangeStatusAttendance(token: String , data : ReqAttendanceChangeStatus): Result<ResAttendanceChangeStatus?> {
        return repo.ChangeStatusAttendance(token , data)
    }


}
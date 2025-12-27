package com.example.keravat.ui.attendance

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.keravat.data.remote.ResAddDescription
import com.example.keravat.data.remote.ResCheckDescription
import com.example.keravat.data.remote.ResReadAttendance
import com.example.keravat.domain.repo.KeravatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repo: KeravatRepo
) : ViewModel() {

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }



    suspend fun readAttendance(token : String): Result<ResReadAttendance?> {

        return repo.readAttendance(token)
    }

    suspend fun addAttendanceDescription(token: String , text: String , date : String) : Result<ResAddDescription?>{
        return repo.addAttendanceDescription(token , text , date)
    }

    suspend fun checkAttendanceDescription(token: String , date : String) : Result<ResCheckDescription  ?>{
        return repo.checkAttendanceDescription(token ,date)
    }
}
package com.example.school.ui.attendance

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.school.data.remote.ResReadAttendance
import com.example.school.domain.repository.AstinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repo: AstinRepo
) : ViewModel() {

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }

    suspend fun readAttendance(token : String): Result<ResReadAttendance?> {

        return repo.readAttendance(token)

    }
}
package com.example.school.ui.main

import android.content.Context
import androidx.compose.ui.graphics.shadow.ShadowContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school.data.remote.ReqAddNotificationToken
import com.example.school.domain.repository.AstinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: AstinRepo
) : ViewModel() {



    fun SendNotificationToken(Req : ReqAddNotificationToken ){
        viewModelScope.launch {
            repo.addNotificationToken(Req.notificationToken, Req.MainToken)
        }
    }

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }
}
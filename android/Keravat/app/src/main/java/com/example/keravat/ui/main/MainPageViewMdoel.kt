package com.example.keravat.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keravat.domain.repo.KeravatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: KeravatRepo
) : ViewModel() {



    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }
}
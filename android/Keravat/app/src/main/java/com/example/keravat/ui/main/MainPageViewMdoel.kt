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

    fun ClearAllPreferences(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    suspend fun getName(context: Context, token: String): String {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val localName = prefs.getString("name", null)

        
        if (!localName.isNullOrEmpty() && localName != "Guest") {
            return localName
        }


        val res = repo.GetName(token)
        val name = res.getOrNull()?.name


        name?.let { prefs.edit().putString("name", it).apply() }


        return name ?: "Guest"
    }





}
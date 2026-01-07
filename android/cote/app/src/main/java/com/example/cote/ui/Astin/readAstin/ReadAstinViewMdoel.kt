package com.example.cote.ui.Astin.readAstin

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.ResReadAstin
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReadAstinViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {


    fun GetNFCStatus(context: Context): String {
        return repo.checkNFCStatus(context)
    }

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        Log.d("token" , token!!)
        return token
    }

    suspend fun ReadNFCUid(context: Context): String? {
        return repo.readNfcTag(context)
    }

    suspend fun ReadAstin(token: String , uid : String): Result<ResReadAstin?> {

        return repo.ReadAstin(token , uid)

    }

}
package com.example.cote.ui.addAstin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cote.data.remote.ReqAddAstin
import com.example.cote.data.remote.ResAddAstin
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class AddAstinViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {


    fun GetNFCStatus(context: Context) : String{
        return repo.checkNFCStatus(context)
    }

    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }

    suspend fun ReadStudent(token : String ): Result<ResReadStudent?> {

        return repo.ReadStudent(token)

    }

    suspend fun ReadNFCUid(context: Context): String? {

        return repo.readNfcTag(context)

    }

    suspend fun AddAstin(token : String , data : ReqAddAstin): Result<ResAddAstin?> {
        return repo.AddAstin(token , data)
    }
}

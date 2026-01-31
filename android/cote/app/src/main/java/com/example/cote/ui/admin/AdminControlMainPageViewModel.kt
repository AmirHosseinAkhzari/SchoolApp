package com.example.cote.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.Admin
import com.example.cote.data.remote.ReqAddAdmin
import com.example.cote.data.remote.ReqAddStudent
import com.example.cote.data.remote.ResAddAdmin
import com.example.cote.data.remote.ResAddStudent
import com.example.cote.data.remote.ResDeleteAdmin
import com.example.cote.data.remote.ResDeleteStudent
import com.example.cote.data.remote.ResReadAdmins
import com.example.cote.data.remote.ResReadClass
import com.example.cote.data.remote.ResReadOneStudent
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.data.remote.ResUpdateStudent
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AdminControlMainPageViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {



    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }

    suspend fun ReadAdmins(token : String ): Result<ResReadAdmins?> {
        return repo.ReadAdmins(token)
    }

    suspend fun AddAdmin(token : String  , admin : ReqAddAdmin): Result<ResAddAdmin?> {
        return repo.AddAdmin(token ,admin )
    }

    suspend fun DeleteAdmin(token : String  , id  : String): Result<ResDeleteAdmin?> {
        return repo.DeleteAdmin(token ,id  )
    }




}
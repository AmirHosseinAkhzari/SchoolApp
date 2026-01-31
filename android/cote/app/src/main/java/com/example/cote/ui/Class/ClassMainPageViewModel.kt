package com.example.cote.ui.Class

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.ResAddClass
import com.example.cote.data.remote.ResAttendanceChangeStatus
import com.example.cote.data.remote.ResChangeClassName
import com.example.cote.data.remote.ResDeleteClass
import com.example.cote.data.remote.ResReadAttendance
import com.example.cote.data.remote.ResReadClass
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import retrofit2.http.DELETE

@HiltViewModel
class ClassMainPageViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {





    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }


    suspend fun ReadClass(token: String ): Result<ResReadClass?> {
        return repo.ReadClass(token)
    }

    suspend fun DeleteClass(token: String  , id : String): Result<ResDeleteClass?> {
        return repo.DeleteClass(token , id )
    }

    suspend fun ChangeClassName(token: String  , id : String  , NewName : String): Result<ResChangeClassName?> {
        return repo.ChangeClassName(token , id  , NewName)
    }

    suspend fun AddClass(token: String  ,  name : String): Result<ResAddClass?> {
        return repo.AddClass(token ,  name)
    }



}
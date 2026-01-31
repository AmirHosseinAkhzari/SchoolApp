package com.example.cote.ui.Student


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cote.data.remote.ReqAddStudent
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.ResAddClass
import com.example.cote.data.remote.ResAddStudent
import com.example.cote.data.remote.ResAttendanceChangeStatus
import com.example.cote.data.remote.ResChangeClassName
import com.example.cote.data.remote.ResDeleteClass
import com.example.cote.data.remote.ResDeleteStudent
import com.example.cote.data.remote.ResReadAttendance
import com.example.cote.data.remote.ResReadClass
import com.example.cote.data.remote.ResReadOneStudent
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.data.remote.ResUpdateStudent
import com.example.cote.data.remote.StudentFullData
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import retrofit2.http.DELETE

@HiltViewModel
class StudentMainPageViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {



    fun GetMainToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return token
    }

    suspend fun ReadStudent(token : String ): Result<ResReadStudent?> {
        return repo.ReadStudent(token)
    }

    suspend fun ReadClass(token: String ): Result<ResReadClass?> {
        return repo.ReadClass(token)
    }

    suspend fun AddStudent(token: String , stu : ReqAddStudent ): Result<ResAddStudent?> {
        return repo.AddStudent(token , stu )
    }

    suspend fun DeleteStudent(token: String , id : String ): Result<ResDeleteStudent?> {
        return repo.DeleteStudent(token , id )
    }

    suspend fun ReadOneStudent(token: String , id : String ): Result<ResReadOneStudent?> {
        return repo.ReadOneStudent(token , id )
    }

    suspend fun UpdateStudent(token: String , id : String , stu : ReqAddStudent ): Result<ResUpdateStudent?> {
        return repo.UpdateStudent(token , id , stu  )
    }






}
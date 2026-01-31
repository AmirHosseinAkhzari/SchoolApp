package com.example.cote.domain.repo

import android.content.Context
import com.example.cote.data.remote.ReqAddAdmin
import com.example.cote.data.remote.ReqAddAstin
import com.example.cote.data.remote.ReqAddStudent
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.ResAddAdmin
import com.example.cote.data.remote.ResAddAstin
import com.example.cote.data.remote.ResAddClass
import com.example.cote.data.remote.ResAddStudent
import com.example.cote.data.remote.ResAttendanceChangeStatus
import com.example.cote.data.remote.ResChangeClassName
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResDeleteAdmin
import com.example.cote.data.remote.ResDeleteClass
import com.example.cote.data.remote.ResDeleteStudent
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadAdmins
import com.example.cote.data.remote.ResReadAstin
import com.example.cote.data.remote.ResReadAttendance
import com.example.cote.data.remote.ResReadClass
import com.example.cote.data.remote.ResReadOneStudent
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.data.remote.ResSendSms
import com.example.cote.data.remote.ResUpdateStudent
import com.example.cote.data.remote.StudentFullData

interface  CoteRepo  {

    //Api

        // login
        suspend fun LoginWithNumber(number: String) : Result<ResOtp?>
        suspend fun LoginCheckOtp(number: String , code : String) : Result<ResCheckOtp?>


        // Student
        suspend fun ReadStudent(token : String) : Result<ResReadStudent?>

        suspend fun AddStudent(token : String , stu : ReqAddStudent) : Result<ResAddStudent?>

        suspend fun DeleteStudent(token : String , id : String) : Result<ResDeleteStudent?>

        suspend fun ReadOneStudent(token : String , id : String) : Result<ResReadOneStudent?>

        suspend fun UpdateStudent(token : String , id : String , stu : ReqAddStudent) : Result<ResUpdateStudent?>


        //admin

        suspend fun ReadAdmins(token : String ) : Result<ResReadAdmins?>

        suspend fun AddAdmin(token : String , admin : ReqAddAdmin ) : Result<ResAddAdmin?>

        suspend fun DeleteAdmin(token : String  , id : String) : Result<ResDeleteAdmin?>



        //Astin
        suspend fun AddAstin(token : String , data : ReqAddAstin) : Result<ResAddAstin?>
        suspend fun ReadAstin(token : String , uid : String) : Result<ResReadAstin?>

        //attendance
        suspend fun SendSms(token : String) : Result<ResSendSms?>

        suspend fun ReadAttendance(token : String , date : String) : Result<ResReadAttendance?>

        suspend fun ChangeStatusAttendance(token : String , data : ReqAttendanceChangeStatus) : Result<ResAttendanceChangeStatus?>


        // class
        suspend fun ReadClass(token : String) : Result<ResReadClass?>

        suspend fun DeleteClass(token : String , id : String) : Result<ResDeleteClass?>

        suspend fun ChangeClassName(token : String , id : String , NewName : String) : Result<ResChangeClassName?>

        suspend fun AddClass(token : String ,  name : String) : Result<ResAddClass?>


        // NFC

        //checker
        fun checkNFCStatus(context: Context) : String

        // reader
        suspend fun readNfcTag(context: Context) : String?
        suspend fun ReadNFCOnlyUID(context: Context): String?


}
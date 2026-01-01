package com.example.cote.domain.repo

import android.content.Context
import com.example.cote.data.remote.ReqAddAstin
import com.example.cote.data.remote.ResAddAstin
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadStudent

interface  CoteRepo  {

    //Api

        // login
        suspend fun LoginWithNumber(number: String) : Result<ResOtp?>
        suspend fun LoginCheckOtp(number: String , code : String) : Result<ResCheckOtp?>


        // Student
        suspend fun ReadStudent(token : String) : Result<ResReadStudent?>

        //Astin
        suspend fun AddAstin(token : String , data : ReqAddAstin) : Result<ResAddAstin?>


    // NFC

        //checker
        fun checkNFCStatus(context: Context) : String

        // reader
        suspend fun readNfcTag(context: Context) : String?


}
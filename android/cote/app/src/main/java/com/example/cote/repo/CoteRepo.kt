package com.example.cote.domain.repo

import android.content.Context
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadStudent

interface  CoteRepo  {

    suspend fun LoginWithNumber(number: String) : Result<ResOtp?>
    suspend fun LoginCheckOtp(number: String , code : String) : Result<ResCheckOtp?>


    suspend fun ReadStudent(token : String) : Result<ResReadStudent?>
    fun checkNFCStatus(context: Context) : String


}
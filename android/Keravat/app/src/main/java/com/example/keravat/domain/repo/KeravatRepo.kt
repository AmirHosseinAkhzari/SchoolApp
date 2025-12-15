package com.example.keravat.domain.repo

import com.example.keravat.data.remote.ResCheckOtp
import com.example.keravat.data.remote.ResOtp
import com.example.keravat.data.remote.ResReadAttendance

interface  KeravatRepo  {

    suspend fun LoginWithNumber(number: String) : Result<ResOtp?>
    suspend fun LoginCheckOtp(number: String , code : String) : Result<ResCheckOtp?>
    suspend fun readAttendance(MainToken : String) : Result<ResReadAttendance?>

}
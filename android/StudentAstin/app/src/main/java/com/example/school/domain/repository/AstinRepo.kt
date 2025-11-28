package com.example.school.domain.repository

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import com.example.school.data.remote.ResAddNotificationToken
import com.example.school.data.remote.ResCheckOtp
import com.example.school.data.remote.ResOtp
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response


interface  AstinRepo  {

    val nfcData: SharedFlow<String>
    var lastUid : String?
    fun readNfc(activity: Activity)
    fun stopReading(activity: Activity)
    suspend fun LoginWithNumber(number: String) : Result<ResOtp?>
    suspend fun LoginWithUid(uid: String) : Result<ResOtp?>
    suspend fun LoginCheckOtp(number: String , code : String) : Result<ResCheckOtp?>
    suspend fun addNotificationToken(notificationToken  : String ,MainToken : String) : Result<ResAddNotificationToken?>

}
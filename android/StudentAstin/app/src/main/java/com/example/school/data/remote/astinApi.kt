package com.example.school.data.remote

import android.media.MediaCodec.QueueRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.POST


data class ReqOtpNum(
    val number: String
)

data class ReqOtpUid(
    val uid: String
)

data class ResOtp(
    val message : String ,
    val code : Int
)

data class ResCheckOtp(
    val message : String ,
    val code : Int ,
    val token : String
)

data class ReqCheckOtp(
    val number : String ,
    val code : String
)

data class ReqAddNotificationToken(
    val notificationToken  : String ,
)

data class ResAddNotificationToken(
    val message  : String ,
    val code : Int
)

data class ResReadAttendance(
    val Info : List<Info> ,
    val total : total
)


data class Info (
    val checkIn : String ,
    val status : String ,
    val date : String
)

data class total (
    val present : Int ,
    val lateness : Int ,
    val absent  : Int
)


interface AstinApi {

    @POST("login/number")
    suspend fun loginWithNumber(@Body request: ReqOtpNum  ) : Response<ResOtp>

    @POST("login/uid")
    suspend fun loginWithUid(@Body request: ReqOtpUid) : Response<ResOtp>


    @POST("login/check")
    suspend fun logincheck(@Body request: ReqCheckOtp ) : Response<ResCheckOtp>

    @POST("notification/addToken")
    suspend fun addNotificationToken(@Body request: ReqAddNotificationToken , @Header("Authorization") token : String ) : Response<ResAddNotificationToken>

    @GET("attendance/read")
    suspend fun readAttendance(@Header("Authorization") token : String ) : Response<ResReadAttendance>

}
package com.example.keravat.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.POST


data class ReqOtpNum(
    val number: String
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

data class ReqAddDescription(
    val text : String ,
    val date : String
)

data class ResAddDescription(
    val message : String ,
    val code : Int
)



interface KeravatApi {

    @POST("login/number")
    suspend fun loginWithNumber(@Body request: ReqOtpNum  ) : Response<ResOtp>

    @POST("login/check")
    suspend fun logincheck(@Body request: ReqCheckOtp ) : Response<ResCheckOtp>

    @GET("attendance/read")
    suspend fun readAttendance(@Header("Authorization") token : String ) : Response<ResReadAttendance>
    
    @POST("attendance/addDescription")
    suspend fun  addAttendanceDescription(@Header("Authorization") token : String , @Body request:ReqAddDescription ) : Response<ResAddDescription>

}
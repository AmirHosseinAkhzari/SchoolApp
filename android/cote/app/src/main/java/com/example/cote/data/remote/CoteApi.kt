package com.example.cote.data.remote

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
    val code : String ,
    val role : String
)



data class ResReadAttendance(
    val Info : List<Info> ,
    val total : total
)


data class Info (
    val checkIn : String ,
    val status : String ,
    val date : String ,
    val description: String? = null
)

data class total (
    val present : Int ,
    val lateness : Int ,
    val absent  : Int
)



data class Student(
    val _id : String ,
    val firstname : String ,
    val lastname : String ,
)

data class ResReadStudent(
    val message: String ,
    val code: Int ,
    val students : List<Student>
)


interface CoteApi {

    @POST("login/number")
    suspend fun loginWithNumber(@Body request: ReqOtpNum  ) : Response<ResOtp>

    @POST("login/check")
    suspend fun logincheck(@Body request: ReqCheckOtp ) : Response<ResCheckOtp>


    @GET("student/read")
    suspend fun ReadStudent(@Header("Authorization") token : String) : Response<ResReadStudent>



}